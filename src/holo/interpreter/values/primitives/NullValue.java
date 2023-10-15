package holo.interpreter.values.primitives;

import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public class NullValue implements Value {
	
	public static final NullValue NULL = new NullValue();
	
	private NullValue() {}
	
	@Override
	public String typeName() {
		return "null";
	}
	
	@Override
	public Value typeOf() { return NULL; }

	@Override
	public boolean isTrue() {
		return false;
	}
	
	@Override
	public boolean equalTo(Value other) {
		return other == NULL;
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
		return "null";
	}
	
	public boolean nullOrUndefined() { return true; }
	
	public Object toJavaObject() { return null; }
	public Class<?> toJavaClass(Object selfObject) { return null; }
	
}
