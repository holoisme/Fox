package holo.errors;

import holo.lang.lexer.Sequence;

public abstract class FoxError {
	
	private final String name;
	private final Sequence sequence;
	
	public FoxError(String name, Sequence sequence) {
		this.name = name;
		this.sequence = sequence;
	}
	
	public void display() {
		System.err.println(textToDisplay());
	}

	public String getName() { return name; }
	public Sequence getSequence() { return sequence; }
	
	public String textToDisplay() {
		return name + ": " + toString() + " at " + sequence;
	}
	
}
