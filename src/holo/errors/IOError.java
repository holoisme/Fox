package holo.errors;

import holo.lang.lexer.Sequence;

public class IOError extends RuntimeError {

	public IOError(String text, Sequence sequence) {
		super("I/O Error", text, sequence);
	}

}
