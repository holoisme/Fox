package holo.errors.trace;

import holo.lang.lexer.Sequence;

public record TraceEntry(String entry, Sequence sequence) {
	
	public String toString() {
		return entry + " at " + sequence;
	}
	
}
