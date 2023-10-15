package holo.interpreter.values.primitives;

import holo.interpreter.values.Value;

public class BooleanValue implements Value {
	
	public static final BooleanValue TRUE = new BooleanValue(true);
	public static final BooleanValue FALSE = new BooleanValue(false);
	
	private final boolean value;
	
	private BooleanValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() { return value; }
	
	public String toString() { return value+""; }
	
	public static BooleanValue get(boolean value) { return value ? TRUE : FALSE; }
	
	@Override
	public boolean isTrue() {
		return value;
	}

	@Override
	public boolean equalTo(Value other) {
		if(other instanceof BooleanValue b)
			return value == b.value;
		return false;
	}
	
	@Override
	public String typeName() { return "boolean"; }
	
	@Override
	public Value typeOf() { return new StringValue("integer"); }
	
	@Override
	public Object toJavaObject() { return value; }
	
	@Override
	public Class<?> toJavaClass(Object selfObject) { return boolean.class; }
	
}
