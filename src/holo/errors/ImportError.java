package holo.errors;

import holo.lang.lexer.Sequence;

public class ImportError extends RuntimeError {

	public ImportError(FoxError error) {
		super(error.toString(), error.getSequence());
	}
	
	public ImportError(String path, Sequence sequence) {
		super("Could not import " + path, sequence);
	}
	
}
