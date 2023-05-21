package holo.interpreter.values.interfaces;

import holo.interpreter.RuntimeResult;
import holo.interpreter.values.Value;

public interface ICallHandler {
	public RuntimeResult callMethod(RuntimeResult onGoing, String name, Value... args);
}
