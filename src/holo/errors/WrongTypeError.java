package holo.errors;

import holo.lang.lexer.Sequence;

public class WrongTypeError extends RuntimeError {

	public WrongTypeError(String expected, String got, Sequence sequence) {
		super("Wrong type", "Expected " + expected + " but got " + got, sequence);
	}

}
