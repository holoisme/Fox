package holo.interpreter.nodes.operations;

import holo.errors.IllegalOperationError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record BinaryOperationNode(BinaryOperationType operation, Node left, Node right, Sequence sequence) implements Node {
	
	public String toString() {
		return "("+left + " " + operation.toString() + " " + right+")";
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value leftValue = onGoingRuntime.register(left.interpret(parentContext, interpreter, onGoingRuntime), left.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		Value rightValue = onGoingRuntime.register(right.interpret(parentContext, interpreter, onGoingRuntime), right.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		Value result = leftValue.binaryOperation(operation, rightValue);
		if(result == null)
			return onGoingRuntime.failure(new IllegalOperationError(operation.toString(), leftValue.typeName() + " and " + rightValue.typeName(), sequence));
		
		return onGoingRuntime.buffer(result);
	}
	
}