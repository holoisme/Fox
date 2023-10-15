package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
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
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value leftValue = left.interpret(parentContext, interpreter);
		return operation == BinaryBooleanOperationType.AND ?
				BooleanValue.get(leftValue.isTrue() && right.interpret(parentContext, interpreter).isTrue()) :
				BooleanValue.get(leftValue.isTrue() || right.interpret(parentContext, interpreter).isTrue());	
	}
	
}