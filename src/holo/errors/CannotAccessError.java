package holo.errors;

import holo.lang.lexer.Sequence;

public class CannotAccessError extends RuntimeError {

	public CannotAccessError(String valueType, Sequence sequence) {
		super("Cannot access on type " + valueType, sequence);
	}

}
