package holo.interpreter.nodes;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface Node {
	
	public Sequence sequence();
	public Value interpret(Context parentContext, Interpreter interpreter);
	
}
