package holo.errors;

import holo.lang.lexer.Sequence;

public class AlreadyExistingVariableError extends RuntimeError {

	public AlreadyExistingVariableError(String varName, Sequence sequence) {
		super("Already existing variable", "Variable '" + varName + "' already declared in this context", sequence);
	}

}
