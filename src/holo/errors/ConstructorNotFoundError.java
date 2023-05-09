package holo.errors;

import holo.lang.lexer.Sequence;

public class ConstructorNotFoundError extends RuntimeError {

	public ConstructorNotFoundError(String className, int forNumberOfArguments, Sequence sequence) {
		super("Constructor not found", "Could not found a constructor of " + className + " with " + forNumberOfArguments + " arguments.", sequence);
	}

}
