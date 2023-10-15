package holo.errors;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import holo.errors.trace.TraceEntry;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.StringValue;
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
	
	public void display(PrintStream out, String originalText) {
		super.display(out, originalText);
		for(int i = trace.size() - 1; i >= 0; i--)
			out.println(trace.get(i).toString());
		out.println("");
	}
	
	public String toString() {
		return text;
	}
	
	public RuntimeError addToTrace(String name, Sequence sequence) {
		trace.add(new TraceEntry(name, sequence));
		return this;
	}

	@Override
	public Value pointGet(String key, Sequence sequence) {
		switch(key) {
		case "name": return new StringValue(getName());
		case "text": return new StringValue(text);
		default: return Value.super.pointGet(key, sequence);
		}
	}
	
	@Override
	public String typeName() {
		return "Error";
	}

	@Override
	public boolean isTrue() { return true; }

	@Override
	public boolean equalTo(Value other) {
		return this == other;
	}

	@Override
	public Value typeOf() { return new StringValue("error"); }

}
