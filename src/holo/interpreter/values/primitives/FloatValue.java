package holo.interpreter.values.primitives;

import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.INumber;

public class FloatValue implements Value, INumber {
	
	protected static final double FLOAT_PRECISION_EPSILON = 0.0000001;
	
	private final float value;
	
	public FloatValue(float value) {
		this.value = value;
	}
	
	public FloatValue(double value) {
		this.value = (float) value;
	}
	
	public float getValue() { return value; }
	
	public String toString() { return value+""; }

	@Override
	public boolean isTrue() {
		return Math.abs(value) > FLOAT_PRECISION_EPSILON;
	}
	
	public Value unaryOperation(UnaryOperationType operation) {
		return operation == UnaryOperationType.NEGATE ? new FloatValue(-value) : null;
	}
	
	public Value binaryOperation(BinaryOperationType operation, Value right) {
		if(operation == BinaryOperationType.AND)
			return BooleanValue.get(isTrue() && right.isTrue());
		else if(operation == BinaryOperationType.OR)
			return BooleanValue.get(isTrue() || right.isTrue());
		else if(operation == BinaryOperationType.PLUS && right instanceof INumber num)
			return new FloatValue(value + num.getFloat());
		else if(operation == BinaryOperationType.MINUS && right instanceof INumber num)
			return new FloatValue(value - num.getFloat());
		else if(operation == BinaryOperationType.MULTIPLY && right instanceof INumber num)
			return new FloatValue(value * num.getFloat());
		else if(operation == BinaryOperationType.DIVIDE && right instanceof INumber num)
			return new FloatValue(value / num.getFloat());
		else if(operation == BinaryOperationType.EXPONANT && right instanceof INumber num)
			return new FloatValue((float) Math.pow(value, num.getFloat()));
		else if(operation == BinaryOperationType.MODULO && right instanceof INumber num)
			return new FloatValue(value % num.getFloat());
		
		else if(operation == BinaryOperationType.DOUBLE_EQUALS && right instanceof INumber num)
			return BooleanValue.get(Math.abs(value - num.getFloat()) <= FLOAT_PRECISION_EPSILON);
		else if(operation == BinaryOperationType.NOT_EQUAL && right instanceof INumber num)
			return BooleanValue.get(Math.abs(value - num.getFloat()) > FLOAT_PRECISION_EPSILON);
		else if(operation == BinaryOperationType.LESS_THAN && right instanceof INumber num)
			return BooleanValue.get(value < num.getFloat());
		else if(operation == BinaryOperationType.GREATER_THAN && right instanceof INumber num)
			return BooleanValue.get(value > num.getFloat());
		else if(operation == BinaryOperationType.LESS_OR_EQUAL && right instanceof INumber num)
			return BooleanValue.get(value <= num.getFloat());
		else if(operation == BinaryOperationType.GREATER_OR_EQUAL && right instanceof INumber num)
			return BooleanValue.get(value >= num.getFloat());
		
		return null;
	}
	
	public Value quickOperation(QuickOperationType operation) {
		if(operation == QuickOperationType.PLUS_PLUS)
			return new FloatValue(value + 1);
		if(operation == QuickOperationType.MINUS_MINUS)
			return new FloatValue(value - 1);
		
		return null;
	}
	
	@Override
	public boolean equalTo(Value other) {
		if(other instanceof FloatValue fv)
			return Math.abs(value - fv.value) <= FLOAT_PRECISION_EPSILON;
		else if(other instanceof IntegerValue iv)
			return Math.abs(value - iv.getValue()) < FLOAT_PRECISION_EPSILON;
		
		return false;
	}
	
	public Value castInto(CastingType type) {
		if(type == CastingType.INTEGER)
			return new IntegerValue((int)value);
		else if(type == CastingType.FLOAT)
			return this;
		else if(type == CastingType.BOOLEAN)
			return BooleanValue.get(isTrue());
		
		return Value.super.castInto(type);
	}

	@Override
	public Value pointGet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value pointSet(String key, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value arrayGet(Value key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value arraySet(Value key, Value value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String typeName() { return "float"; }

	@Override
	public int getInteger() {
		return (int) value;
	}

	@Override
	public float getFloat() {
		return value;
	}
	
	public Object toJavaObject() { return value; }
	public Class<?> toJavaClass(Object selfObject) { return float.class; }
	
}
