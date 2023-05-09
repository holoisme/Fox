package holo.interpreter.values.primitives;

import holo.interpreter.values.Value;

public class NullValue implements Value {
	
	public NullValue() {}
	
	@Override
	public String typeName() {
		return "null";
	}

	@Override
	public boolean isTrue() {
		return false;
	}
	
	@Override
	public boolean equalTo(Value other) {
		return other == Value.NULL;
	}

	@Override
	public Value pointGet(String key) {
		return null;
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
	
	@Override
	public String toString() {
		return "null";
	}
	
	public Object toJavaObject() { return null; }
	public Class<?> toJavaClass(Object selfObject) { return null; }
	
}
