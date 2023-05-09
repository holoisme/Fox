package holo.errors;

import java.util.ArrayList;
import java.util.List;

import holo.errors.trace.TraceEntry;
import holo.lang.lexer.Sequence;

public class RuntimeError extends FoxError {
	
	private String text;
	
	private final List<TraceEntry> trace;
	
	public RuntimeError(String text, Sequence sequence) {
		super("Runtime Error", sequence);
		this.text = text;
		this.trace = new ArrayList<>();
	}
	
	public RuntimeError(String name, String text, Sequence sequence) {
		super(name, sequence);
		this.text = text;
		this.trace = new ArrayList<>();
	}
	
	public String toString() {
		return text;
	}
	
	public RuntimeError addToTrace(String name, Sequence sequence) {
		trace.add(new TraceEntry(name, sequence));
		return this;
	}

}
