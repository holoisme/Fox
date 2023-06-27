package holo.interpreter.values.interfaces;

import holo.interpreter.values.Value;

public interface ICallHandler {
	public Value callMethod(String name, Value... args);
}
