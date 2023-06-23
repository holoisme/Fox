package holo.interpreter.values.interfaces;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface IInstanciable {
	
	public RuntimeResult createInstance(Interpreter interpreter, RuntimeResult onGoing, Sequence sequence, Value... args);
	
}
