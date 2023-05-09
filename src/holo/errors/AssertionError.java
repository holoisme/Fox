package holo.errors;

import holo.lang.lexer.Sequence;

public class AssertionError extends RuntimeError {

	public AssertionError(String text, Sequence sequence) {
		super("Assertion Error", text, sequence);
	}

}
