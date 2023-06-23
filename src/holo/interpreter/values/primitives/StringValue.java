package holo.interpreter.values.primitives;

import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IIterable;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.prototypes.StringPrototype;

public class StringValue implements Value, IIterable {
	
	private final String value;
	
	public StringValue(String value) {
		this.value = value;
	}
	
	public String getValue() { return value; }
	
	public String toString() { return value; }
	
	public Value binaryOperation(BinaryOperationType operation, Value right) {
		if(operation == BinaryOperationType.PLUS)
			return new StringValue(value + right.toString());
		else if(operation == BinaryOperationType.MULTIPLY && right instanceof INumber num)
			return new StringValue(value.repeat(num.getInteger()));
		else if(operation == BinaryOperationType.DOUBLE_EQUALS && right instanceof StringValue)
			return BooleanValue.get(value.equals(right.toString()));
		
		return Value.super.binaryOperation(operation, right);
	}
	
	public Value castInto(CastingType type) {
		try {
			if(type == CastingType.INTEGER)
				return IntegerValue.get(Integer.parseInt(value));
			else if(type == CastingType.FLOAT)
				return new FloatValue(Float.parseFloat(value));
			else if(type == CastingType.DOUBLE)
				return new DoubleValue(Double.parseDouble(value));
			else if(type == CastingType.BOOLEAN)
				return BooleanValue.get(Boolean.parseBoolean(value));
		} catch(Exception e) { return null; }
		
		return Value.super.castInto(type);
	}
	
	@Override
	public boolean isTrue() {
		return !value.isEmpty();
	}

	@Override
	public boolean equalTo(Value other) {
		return value.equals(other.toString());
	}

	@Override
	public Value pointGet(String key) {
		return StringPrototype.PROTOTYPE.get(key);
	}

	@Override
	public Value pointSet(String key, Value value) {
		return null;
	}

	@Override
	public Value arrayGet(Value key) {
		if(key instanceof INumber num && num.isInteger())
			return new CharValue(value.charAt(num.getInteger()));
		return null;
	}

	@Override
	public Value arraySet(Value key, Value value) {
		return null;
	}
	
	public String typeName() { return "string"; }

	@Override
	public Value elementAt(int index) {
		return new CharValue(value.charAt(index));
	}

	@Override
	public boolean hasReachedEnd(int index) {
		return index >= value.length();
	}

	public int length() {
		return value.length();
	}
	
	public Object toJavaObject() { return value; }
	public Class<?> toJavaClass(Object selfObject) { return String.class; }
	
}
