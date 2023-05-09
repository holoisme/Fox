package holo.errors;

import holo.lang.lexer.Sequence;

public class IndexOutOfBoundsError extends RuntimeError {

	public IndexOutOfBoundsError(int index, int size, Sequence sequence) {
		super("Index Out of Bounds", "Tried to access index " + index + " on size " + size, sequence);
	}

}
