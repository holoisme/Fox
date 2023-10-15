package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record NotNode(Node node, Sequence sequence) implements Node {
	
	public String toString() {
		return "not " + node;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		return BooleanValue.get(!node.interpret(parentContext, interpreter).isTrue());
	}
	
}