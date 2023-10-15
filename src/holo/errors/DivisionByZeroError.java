package holo.errors;

import holo.lang.lexer.Sequence;

public class DivisionByZeroError extends RuntimeError {

	public DivisionByZeroError(Sequence sequence) {
		super("Division by zero", "/ by zero", sequence);
	}
	
}
