package holo.errors;

import holo.lang.lexer.Sequence;

public class NoThisError extends RuntimeError {

	public NoThisError(Sequence sequence) {
		super("No This Reference", "No 'this' to reference from this sequence", sequence);
	}
	
}
