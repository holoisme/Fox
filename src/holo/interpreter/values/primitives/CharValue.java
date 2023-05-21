package holo.interpreter.values.primitives;

import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.INumber;

public class CharValue implements Value, INumber {
	
	private final char value;
	
	public CharValue(char value) {
		this.value = value;
	}
	
	public char getValue() { return value; }
	
	public String toString() { return Character.toString(value); }
	
	public Value binaryOperation(BinaryOperationType operation, Value right) {
		if(operation == BinaryOperationType.AND)
			return BooleanValue.get(isTrue() && right.isTrue());
		else if(operation == BinaryOperationType.OR)
			return BooleanValue.get(isTrue() || right.isTrue());
		
		if(right instanceof INumber num) {
			switch (operation) {
				case PLUS:
					return num.isInteger() ? IntegerValue.get(value + num.getInteger()) : num.isDouble() ? new DoubleValue(value + num.getDouble()) : new FloatValue(value + num.getFloat());
				case MINUS:
					return num.isInteger() ? IntegerValue.get(value - num.getInteger()) : num.isDouble() ? new DoubleValue(value - num.getDouble()) : new FloatValue(value - num.getFloat());
				case MULTIPLY:
					return num.isInteger() ? IntegerValue.get(value * num.getInteger()) : num.isDouble() ? new DoubleValue(value * num.getDouble()) : new FloatValue(value * num.getFloat());
				case DIVIDE:
					return num.isInteger() ? IntegerValue.get(value / num.getInteger()) : num.isDouble() ? new DoubleValue(value / num.getDouble()) : new FloatValue(value / num.getFloat());
				case EXPONANT:
					return num.isInteger() ? IntegerValue.get((int) Math.pow(value, num.getInteger())) : num.isDouble() ? new DoubleValue(Math.pow(value, num.getDouble())) : new FloatValue((float) Math.pow(value, num.getFloat()));
				case MODULO:
					return num.isInteger() ? IntegerValue.get(value % num.getInteger()) : num.isDouble() ? new DoubleValue(value % num.getDouble()) : new FloatValue(value % num.getFloat());
				
				case DOUBLE_EQUALS:
					return BooleanValue.get(num.isInteger() ? value == num.getInteger() : Math.abs(value - num.getFloat()) <= FloatValue.FLOAT_PRECISION_EPSILON);
				case NOT_EQUAL:
					return BooleanValue.get(num.isInteger() ? value != num.getInteger() : Math.abs(value - num.getFloat()) > FloatValue.FLOAT_PRECISION_EPSILON);
				case LESS_THAN:
					return BooleanValue.get(value < num.getFloat());
				case GREATER_THAN:
					return BooleanValue.get(value > num.getFloat());
				case LESS_OR_EQUAL:
					return BooleanValue.get(value <= num.getFloat());
				case GREATER_OR_EQUAL:
					return BooleanValue.get(value >= num.getFloat());
				
				default: break;
			}
			
		}
		
		return Value.super.binaryOperation(operation, right);
	}
	
	public Value quickOperation(QuickOperationType operation) {
		if(operation == QuickOperationType.PLUS_PLUS)
			return new CharValue((char) (value + 1));
		if(operation == QuickOperationType.MINUS_MINUS)
			return new CharValue((char) (value - 1));
		
		return null;
	}
	
	public Value castInto(CastingType type) {
		if(type == CastingType.INTEGER)
			return IntegerValue.get(value);
		else if(type == CastingType.FLOAT)
			return new FloatValue(value);
		else if(type == CastingType.DOUBLE)
			return new DoubleValue(value);
		else if(type == CastingType.CHARACTER)
			return this;
		
		return Value.super.castInto(type);
	}
	
	@Override
	public boolean isTrue() {
		return value != 0;
	}
	
	@Override
	public boolean equalTo(Value other) {
		if(other instanceof CharValue iv)
			return value == iv.value;
		
		return false;
	}

	@Override
	public Value pointGet(String key) { return null; }

	@Override
	public Value pointSet(String key, Value value) { return null; }

	@Override
	public Value arrayGet(Value key) { return null; }

	@Override
	public Value arraySet(Value key, Value value) { return null; }
	
	public String typeName() { return "char"; }

	@Override
	public int getInteger() {
		return value;
	}

	@Override
	public float getFloat() {
		return value;
	}
	
	@Override
	public double getDouble() {
		return value;
	}
	
	@Override
	public Object toJavaObject() { return value; }
	
	@Override
	public Class<?> toJavaClass(Object selfObject) { return char.class; }

	@Override
	public NumberType numberType() {
		return NumberType.CHAR;
	}

	@Override
	public boolean isInteger() {
		return true;
	}
	
}
