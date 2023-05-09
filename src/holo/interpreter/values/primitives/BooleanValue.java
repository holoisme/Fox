package holo.interpreter.values.primitives;

import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.UnaryOperationType;
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
	
	public static BooleanValue get(boolean value) { return value?TRUE:FALSE; }
	
	public Value unaryOperation(UnaryOperationType operation) {
		return operation == UnaryOperationType.NOT ? get(!value) : null;
	}
	
	public Value binaryOperation(BinaryOperationType operation, Value right) {
		if(operation == BinaryOperationType.AND)
			return get(value && right.isTrue());
		else if(operation == BinaryOperationType.OR)
			return get(value || right.isTrue());
		
		return null;
	}
	
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
	
	public String typeName() { return "boolean"; }
	
	public Object toJavaObject() { return value; }
	public Class<?> toJavaClass(Object selfObject) { return boolean.class; }
	
}
