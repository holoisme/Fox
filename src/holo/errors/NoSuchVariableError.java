package holo.errors;

import holo.lang.lexer.Sequence;

public class NoSuchVariableError extends RuntimeError {

	public NoSuchVariableError(String varName, Sequence sequence) {
		super("No such variable as " + varName, sequence);
	}

}
