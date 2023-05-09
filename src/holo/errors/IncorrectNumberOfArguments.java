package holo.errors;

import holo.lang.lexer.Sequence;

public class IncorrectNumberOfArguments extends RuntimeError {

	public IncorrectNumberOfArguments(int expectedNumberOfArguments, int actualNumberOfArguments, String functionName, Sequence sequence) {
		super("Incorrect number of arguments", "Expected " + expectedNumberOfArguments + " but got " + actualNumberOfArguments + " for " + functionName, sequence);
	}

}
