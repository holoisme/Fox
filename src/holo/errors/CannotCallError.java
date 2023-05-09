package holo.errors;

import holo.lang.lexer.Sequence;

public class CannotCallError extends RuntimeError {
	
	public CannotCallError(String valueType, Sequence sequence) {
		super("Cannot call on type " + valueType, sequence);
	}
	
}
