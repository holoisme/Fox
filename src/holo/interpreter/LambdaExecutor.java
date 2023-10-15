package holo.interpreter;

import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface LambdaExecutor {
	
	public Value call(Value host, Context context, Interpreter interpreter, Sequence sequence, Value... args);
	
}
