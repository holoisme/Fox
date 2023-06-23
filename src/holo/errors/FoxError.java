package holo.errors;

import java.io.PrintStream;
import java.util.Objects;

import holo.lang.lexer.Sequence;

public abstract class FoxError {
	
	private final String name;
	private final Sequence sequence;
	
	public FoxError(String name, Sequence sequence) {
		Objects.requireNonNull(sequence);
		this.name = name;
		this.sequence = sequence;
	}
	
	public void display(PrintStream out) {
		out.println(textToDisplay());
	}
	
	public void display(PrintStream out, String originalText) {
		display(out);
		out.println(sequence.arrowify(originalText));
	}

	public String getName() { return name; }
	public Sequence getSequence() { return sequence; }
	
	public String textToDisplay() {
		return name + ": " + toString() + " at " + sequence;
	}
	
}
