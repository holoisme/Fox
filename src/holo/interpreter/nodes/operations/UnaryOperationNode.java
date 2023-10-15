package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record UnaryOperationNode(UnaryOperationType operation, Node node, Sequence sequence) implements Node {
	
	public String toString() {
		return operation + "" + node;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		return node.interpret(parentContext, interpreter).unaryOperation(operation, interpreter, sequence);
	}
	
}