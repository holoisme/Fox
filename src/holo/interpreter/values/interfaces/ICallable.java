package holo.interpreter.values.interfaces;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;

public interface ICallable {
	
	public RuntimeResult call(Value host, Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime, Value... args);
	public int numberOfArguments();
	
}
