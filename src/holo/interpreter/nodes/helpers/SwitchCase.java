package holo.interpreter.nodes.helpers;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface SwitchCase {
	
	Node expression();
	RuntimeResult doesMatch(Value value, Context context, Interpreter interpreter, RuntimeResult onGoingRuntime);
	Sequence sequence();
	
}
