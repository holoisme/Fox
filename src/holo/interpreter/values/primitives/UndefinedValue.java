package holo.interpreter.values.primitives;

import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public class UndefinedValue implements Value {
	
	public static final UndefinedValue UNDEFINED = new UndefinedValue();
	
	private UndefinedValue() {}
	
	@Override
	public String typeName() {
		return "undefined";
	}

	@Override
	public boolean isTrue() {
		return false;
	}
	
	@Override
	public boolean equalTo(Value other) {
		return other == UNDEFINED;
	}

	@Override
	public Value pointGet(String key, Sequence sequence) {
		return null;
	}

	@Override
	public Value pointSet(String key, Value value, Sequence sequence) {
		return null;
	}

	@Override
	public Value arrayGet(Value key, Sequence sequence) {
		return null;
	}

	@Override
	public Value arraySet(Value key, Value value, Sequence sequence) {
		return null;
	}
	
	@Override
	public String toString() {
		return "undefined";
	}
	
	@Override
	public Value typeOf() { return UNDEFINED; }
	
	public boolean nullOrUndefined() { return true; }
	
	public Object toJavaObject() { return null; }
	public Class<?> toJavaClass(Object selfObject) { return null; }
	
}
