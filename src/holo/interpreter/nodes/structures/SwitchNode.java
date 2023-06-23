package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.SwitchCase;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record SwitchNode(Node valueNode, SwitchCase[] cases, Node catchNode, Sequence sequence) implements Node {
	
	public String toString() {
		return "switch("+valueNode+") {\n" + ReflectionUtils.toString(cases, "\n") + "\n" + (catchNode != null ? "catch: " + catchNode + "\n" : "") + "}";
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value value = onGoingRuntime.register(valueNode.interpret(parentContext, interpreter, onGoingRuntime), valueNode.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		for(SwitchCase c : cases) {
			Value bool = onGoingRuntime.register(c.doesMatch(value, parentContext, interpreter, onGoingRuntime), c.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			if(bool == BooleanValue.TRUE) {
				Node expression = c.expression();
				
				Value toReturn = onGoingRuntime.register(expression.interpret(parentContext, interpreter, onGoingRuntime), expression.sequence());
				if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
				
				return onGoingRuntime.buffer(toReturn);
			}
		}
		
		if(catchNode != null) {
			Value toReturn = onGoingRuntime.register(catchNode.interpret(parentContext, interpreter, onGoingRuntime), catchNode.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			return onGoingRuntime.buffer(toReturn);
		}
		
		return onGoingRuntime.buffer(Value.NULL);
	}
	
}