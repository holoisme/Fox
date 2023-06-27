package holo.interpreter;

import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;

public interface LambdaExecutor {
	
	public Value call(Value host, Context context, Interpreter interpreter, Value... args);
	
}
