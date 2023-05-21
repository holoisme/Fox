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
	
	public float getValue() { return value; }
	
	public String toString() { return value+"f"; }

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
		
		if(right instanceof INumber num) {
			switch (operation) {
				case PLUS:
					return num.isDouble() ? new DoubleValue(value + num.getDouble()) : new FloatValue(value + num.getFloat());
				case MINUS:
					return num.isDouble() ? new DoubleValue(value - num.getDouble()) : new FloatValue(value - num.getFloat());
				case MULTIPLY:
					return num.isDouble() ? new DoubleValue(value * num.getDouble()) : new FloatValue(value * num.getFloat());
				case DIVIDE:
					return num.isDouble() ? new DoubleValue(value / num.getDouble()) : new FloatValue(value / num.getFloat());
				case EXPONANT:
					return num.isDouble() ? new DoubleValue(Math.pow(value, num.getDouble())) : new FloatValue((float) Math.pow(value, num.getFloat()));
				case MODULO:
					return num.isDouble() ? new DoubleValue(value % num.getDouble()) : new FloatValue(value % num.getFloat());
				
				case DOUBLE_EQUALS:
					return BooleanValue.get(Math.abs(value - num.getFloat()) <= FLOAT_PRECISION_EPSILON);
				case NOT_EQUAL:
					return BooleanValue.get(Math.abs(value - num.getFloat()) > FLOAT_PRECISION_EPSILON);
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
		if(other instanceof INumber num)
			return Math.abs(value - num.getFloat()) <= FLOAT_PRECISION_EPSILON;
		
		return false;
	}
	
	public Value castInto(CastingType type) {
		if(type == CastingType.INTEGER)
			return IntegerValue.get((int)value);
		else if(type == CastingType.FLOAT)
			return this;
		else if(type == CastingType.DOUBLE)
			return new DoubleValue(value);
		
		return Value.super.castInto(type);
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
	
	public String typeName() { return "float"; }

	@Override
	public int getInteger() {
		return (int) value;
	}

	@Override
	public float getFloat() {
		return value;
	}
	
	@Override
	public double getDouble() {
		return value;
	}
	
	public Object toJavaObject() { return value; }
	public Class<?> toJavaClass(Object selfObject) { return float.class; }
	
	@Override
	public NumberType numberType() {
		return NumberType.FLOAT;
	}

	@Override
	public boolean isInteger() {
		return false;
	}
	
}
