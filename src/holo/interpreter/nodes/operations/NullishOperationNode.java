package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record NullishOperationNode(Node left, Node right, Sequence sequence) implements Node {
	
	public String toString() {
		return left+" ?? "+right;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value leftValue = left.interpret(parentContext, interpreter);
		
		return leftValue.isTrue() ? leftValue : right.interpret(parentContext, interpreter);
	}
	
}