package holo.interpreter.nodes;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.lang.lexer.Sequence;

public interface Node {
	
	public Sequence sequence();
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime);
	
}
