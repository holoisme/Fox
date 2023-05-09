package holo.interpreter.values.interfaces;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.values.Value;

public interface IInstanciable {
	
	public RuntimeResult createInstance(Interpreter interpreter, RuntimeResult onGoing, Value... args);
	
}
