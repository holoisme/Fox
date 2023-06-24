package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.BinaryBooleanOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record BinaryBooleanOperationNode(BinaryBooleanOperationType operation, Node left, Node right, Sequence sequence) implements Node {
	
	public String toString() {
		return left + " " + operation.toString() + " " + right;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value leftValue = onGoingRuntime.register(left.interpret(parentContext, interpreter, onGoingRuntime), left.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		if(operation == BinaryBooleanOperationType.AND) {
			if(!leftValue.isTrue())
				return onGoingRuntime.buffer(BooleanValue.FALSE);
			
			Value rightValue = onGoingRuntime.register(right.interpret(parentContext, interpreter, onGoingRuntime), right.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			return onGoingRuntime.buffer(BooleanValue.get(leftValue.isTrue() && rightValue.isTrue()));
		} else if(operation == BinaryBooleanOperationType.OR) {
			if(leftValue.isTrue())
				return onGoingRuntime.buffer(BooleanValue.TRUE);
			
			Value rightValue = onGoingRuntime.register(right.interpret(parentContext, interpreter, onGoingRuntime), right.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			return onGoingRuntime.buffer(BooleanValue.get(leftValue.isTrue() || rightValue.isTrue()));
		}
		
		return onGoingRuntime.buffer(Value.NULL);
	}
	
}