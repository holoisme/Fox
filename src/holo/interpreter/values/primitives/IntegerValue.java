package holo.interpreter.values.primitives;

import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.INumber;

public class IntegerValue implements Value, INumber {
	
	public static final IntegerValue ZERO = new IntegerValue(0),
									 ONE = new IntegerValue(1);
	
	private final int value;
	
	public IntegerValue(int value) {
		this.value = value;
	}
	
	public int getValue() { return value; }
	
	public String toString() { return value+""; }
	
	public Value unaryOperation(UnaryOperationType operation) {
		return operation == UnaryOperationType.NEGATE ? new IntegerValue(-value) : null;
	}
	
	public Value binaryOperation(BinaryOperationType operation, Value right) {
		if(operation == BinaryOperationType.AND)
			return BooleanValue.get(isTrue() && right.isTrue());
		else if(operation == BinaryOperationType.OR)
			return BooleanValue.get(isTrue() || right.isTrue());
		else if(operation == BinaryOperationType.PLUS && right instanceof INumber num)
			return num.isInteger() ? new IntegerValue(value + num.getInteger()) : new FloatValue(value + num.getFloat());
		else if(operation == BinaryOperationType.MINUS && right instanceof INumber num)
			return num.isInteger() ? new IntegerValue(value - num.getInteger()) : new FloatValue(value - num.getFloat());
		else if(operation == BinaryOperationType.MULTIPLY && right instanceof INumber num)
			return num.isInteger() ? new IntegerValue(value * num.getInteger()) : new FloatValue(value * num.getFloat());
		else if(operation == BinaryOperationType.DIVIDE && right instanceof INumber num)
			return num.isInteger() ? new IntegerValue(value / num.getInteger()) : new FloatValue(value / num.getFloat());
		else if(operation == BinaryOperationType.EXPONANT && right instanceof INumber num)
			return num.isInteger() ? new IntegerValue((int) Math.pow(value, num.getInteger())) : new FloatValue((float) Math.pow(value, num.getFloat()));
		else if(operation == BinaryOperationType.MODULO && right instanceof INumber num)
			return num.isInteger() ? new IntegerValue(value % num.getInteger()) : new FloatValue(value % num.getFloat());
		
		else if(operation == BinaryOperationType.DOUBLE_EQUALS && right instanceof INumber num)
			return BooleanValue.get(num.isInteger() ? value == num.getInteger() : Math.abs(value - num.getFloat()) <= FloatValue.FLOAT_PRECISION_EPSILON);
		else if(operation == BinaryOperationType.NOT_EQUAL && right instanceof INumber num)
			return BooleanValue.get(num.isInteger() ? value != num.getInteger() : Math.abs(value - num.getFloat()) > FloatValue.FLOAT_PRECISION_EPSILON);
		else if(operation == BinaryOperationType.LESS_THAN && right instanceof INumber num)
			return BooleanValue.get(value < num.getFloat());
		else if(operation == BinaryOperationType.GREATER_THAN && right instanceof INumber num)
			return BooleanValue.get(value > num.getFloat());
		else if(operation == BinaryOperationType.LESS_OR_EQUAL && right instanceof INumber num)
			return BooleanValue.get(value <= num.getFloat());
		else if(operation == BinaryOperationType.GREATER_OR_EQUAL && right instanceof INumber num)
			return BooleanValue.get(value >= num.getFloat());
		
		return Value.super.binaryOperation(operation, right);
	}
	
	public Value quickOperation(QuickOperationType operation) {
		if(operation == QuickOperationType.PLUS_PLUS)
			return new IntegerValue(value + 1);
		if(operation == QuickOperationType.MINUS_MINUS)
			return new IntegerValue(value - 1);
		
		return null;
	}
	
	public Value castInto(CastingType type) {
		if(type == CastingType.INTEGER)
			return this;
		else if(type == CastingType.FLOAT)
			return new FloatValue(value);
		else if(type == CastingType.BOOLEAN)
			return BooleanValue.get(isTrue());
		
		return Value.super.castInto(type);
	}
	
	@Override
	public boolean isTrue() {
		return value != 0;
	}
	
	@Override
	public boolean equalTo(Value other) {
		if(other instanceof FloatValue fv)
			return Math.abs(value - fv.getValue()) < FloatValue.FLOAT_PRECISION_EPSILON;
		else if(other instanceof IntegerValue iv)
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
	
	public String typeName() { return "integer"; }

	@Override
	public int getInteger() {
		return value;
	}

	@Override
	public float getFloat() {
		return value;
	}
	
	public Object toJavaObject() { return value; }
	public Class<?> toJavaClass(Object selfObject) { return int.class; }
	
}
