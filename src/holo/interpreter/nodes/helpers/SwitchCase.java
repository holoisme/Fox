package holo.interpreter.nodes.helpers;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface SwitchCase {
	
	Node expression();
	boolean doesMatch(Value value, Context context, Interpreter interpreter);
	Sequence sequence();
	
}
