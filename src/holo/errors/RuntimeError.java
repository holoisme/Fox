package holo.errors;

import java.util.ArrayList;
import java.util.List;

import holo.errors.trace.TraceEntry;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public class RuntimeError extends FoxError implements Value {
	
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

	@Override
	public String typeName() {
		return "Error";
	}

	@Override
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		return this == other;
	}

	@Override
	public Value pointGet(String key) {
		return null; // TODO !
	}

	@Override
	public Value pointSet(String key, Value value) {
		return null;
	}

	@Override
	public Value arrayGet(Value key) {
		return null;
	}

	@Override
	public Value arraySet(Value key, Value value) {
		return null;
	}

}
