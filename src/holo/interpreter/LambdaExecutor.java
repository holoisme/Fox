package holo.interpreter;

import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;

public interface LambdaExecutor {
	
	public RuntimeResult call(Value host, Context context, Interpreter interpreter, RuntimeResult onGoingRuntime, Value... args);
	
}
