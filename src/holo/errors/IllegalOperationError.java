package holo.errors;

import holo.lang.lexer.Sequence;

public class IllegalOperationError extends RuntimeError {

	public IllegalOperationError(String operation, String valueType, Sequence sequence) {
		super("Illegal Operation", "Operation " + operation + " illegal on type " + valueType, sequence);
	}

}
