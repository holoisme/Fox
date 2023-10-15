package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record BinaryOperationNode(BinaryOperationType operation, Node left, Node right, Sequence sequence) implements Node {
	
	public String toString() {
		return "(" + left + " " + operation.toString() + " " + right + ")";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		return left.interpret(parentContext, interpreter).binaryOperation(operation, right.interpret(parentContext, interpreter), interpreter, sequence);
	}
	
}