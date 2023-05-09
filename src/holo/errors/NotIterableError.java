package holo.errors;

import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public class NotIterableError extends RuntimeError {

	public NotIterableError(Value value, Sequence sequence) {
		super("Not Iterable", "Cannot go trough " + value + " ("+value.typeName()+") as an iterable", sequence);
	}

}
