package holo.interpreter.nodes.operations;

import holo.errors.IllegalOperationError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record BinaryOperationNode(BinaryOperationType operation, Node left, Node right, Sequence sequence) implements Node {
	
	public String toString() {
		return left + " " + operation.toString() + " " + right;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value leftValue = left.interpret(parentContext, interpreter);
		Value rightValue = right.interpret(parentContext, interpreter);
		
		Value result = leftValue.binaryOperation(operation, rightValue);
		
		if(result == null)
			throw new TError(new IllegalOperationError(operation.toString(), leftValue.typeName() + " and " + rightValue.typeName(), sequence));
		
		return result;
	}
	
}