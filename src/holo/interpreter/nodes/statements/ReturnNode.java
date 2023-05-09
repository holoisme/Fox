package holo.interpreter.nodes.statements;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record ReturnNode(Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return "return " + (expression!=null?expression:"");
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value valueToReturn = Value.NULL;
		if(expression != null) {
			valueToReturn = onGoingRuntime.register(expression.interpret(parentContext, interpreter, onGoingRuntime), expression.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		
		return onGoingRuntime.encounterReturn(valueToReturn);
	}
	
}