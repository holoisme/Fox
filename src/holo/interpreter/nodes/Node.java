package holo.interpreter.nodes;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface Node {
	
	Sequence sequence();
	Value interpret(Context parentContext, Interpreter interpreter);
	
	public default Sequence join(Node node) {
		return sequence().join(node == null ? null : node.sequence());
	}
	
}
