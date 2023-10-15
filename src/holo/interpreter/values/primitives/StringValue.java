package holo.interpreter.values.primitives;

import holo.interpreter.Interpreter;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IIterable;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.primitives.numbers.CharValue;
import holo.interpreter.values.primitives.numbers.DoubleValue;
import holo.interpreter.values.primitives.numbers.FloatValue;
import holo.interpreter.values.primitives.numbers.IntegerValue;
import holo.interpreter.values.prototypes.StringPrototype;
import holo.lang.lexer.Sequence;

public class StringValue implements Value, IIterable {
	
	private final String value;
	
	public StringValue(String value) {
		this.value = value;
	}
	
	public String getValue() { return value; }
	
	public String toString() { return value; }
	
	@Override
	public Value binaryOperation(BinaryOperationType operation, Value right, Interpreter interpreter, Sequence sequence) {
		if(operation == BinaryOperationType.PLUS)
			return new StringValue(value + right.toString());
		else if(operation == BinaryOperationType.MULTIPLY && right instanceof INumber num)
			return new StringValue(value.repeat(num.getInteger()));
		else if(operation == BinaryOperationType.DOUBLE_EQUALS && right instanceof StringValue)
			return BooleanValue.get(value.equals(right.toString()));
		
		return Value.super.binaryOperation(operation, right, interpreter, sequence);
	}
	
	@Override
	public Value castInto(CastingType type, Interpreter interpreter, Sequence sequence) {
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
		
		return Value.super.castInto(type, interpreter, sequence);
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
	public Value pointGet(String key, Sequence sequence) {
		return StringPrototype.PROTOTYPE.get(key);
	}

	@Override
	public Value arrayGet(Value key, Sequence sequence) {
		if(key instanceof INumber num)
			return new CharValue(value.charAt(num.getInteger()));
		return null;
	}

	@Override
	public Value arraySet(Value key, Value value, Sequence sequence) {
		return null;
	}
	
	@Override
	public String typeName() { return "string"; }
	
	@Override
	public Value typeOf() { return new StringValue("string"); }

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
