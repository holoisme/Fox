package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record TernaryOperationNode(Node condition, Node ifTrue, Node ifFalse, Sequence sequence) implements Node {
	
	public String toString() {
		return condition + " ? " + ifTrue + " : " + ifFalse;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value conditionValue = onGoingRuntime.register(condition.interpret(parentContext, interpreter, onGoingRuntime), condition.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		if(conditionValue.isTrue()) {
			Value ifTrueValue = onGoingRuntime.register(ifTrue.interpret(parentContext, interpreter, onGoingRuntime), ifTrue.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			return onGoingRuntime.buffer(ifTrueValue);
		} else {
			Value ifFalseValue = onGoingRuntime.register(ifFalse.interpret(parentContext, interpreter, onGoingRuntime), ifFalse.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			return onGoingRuntime.buffer(ifFalseValue);
		}
	}
	
}