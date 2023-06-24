package holo.interpreter.values.primitives;

import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.INumber;

public class DoubleValue implements Value, INumber {
	
	protected static final double DOUBLE_PRECISION_EPSILON = 0.0000001;
	
	private final double value;
	
	public DoubleValue(double value) {
		this.value = value;
	}
	
	public double getValue() { return value; }
	
	public String toString() { return value+""; }

	@Override
	public boolean isTrue() {
		return Math.abs(value) > DOUBLE_PRECISION_EPSILON;
	}
	
	public Value unaryOperation(UnaryOperationType operation) {
		return operation == UnaryOperationType.NEGATE ? new DoubleValue(-value) : null;
	}
	
	public Value binaryOperation(BinaryOperationType operation, Value right) {
		if(right instanceof INumber num) {
			switch (operation) {
				case PLUS:
					return new DoubleValue(value + num.getDouble());
				case MINUS:
					return new DoubleValue(value - num.getDouble());
				case MULTIPLY:
					return new DoubleValue(value * num.getDouble());
				case DIVIDE:
					return new DoubleValue(value / num.getDouble());
				case EXPONANT:
					return new DoubleValue(Math.pow(value, num.getDouble()));
				case MODULO:
					return new DoubleValue(value % num.getDouble());
				
				case DOUBLE_EQUALS:
					return BooleanValue.get(Math.abs(value - num.getDouble()) <= DOUBLE_PRECISION_EPSILON);
				case NOT_EQUAL:
					return BooleanValue.get(Math.abs(value - num.getDouble()) > DOUBLE_PRECISION_EPSILON);
				case LESS_THAN:
					return BooleanValue.get(value < num.getDouble());
				case GREATER_THAN:
					return BooleanValue.get(value > num.getDouble());
				case LESS_OR_EQUAL:
					return BooleanValue.get(value <= num.getDouble());
				case GREATER_OR_EQUAL:
					return BooleanValue.get(value >= num.getDouble());
				
				default: break;
			}
		}
		
		
		return Value.super.binaryOperation(operation, right);
	}
	
	public Value quickOperation(QuickOperationType operation) {
		if(operation == QuickOperationType.PLUS_PLUS)
			return new DoubleValue(value + 1);
		if(operation == QuickOperationType.MINUS_MINUS)
			return new DoubleValue(value - 1);
		
		return null;
	}
	
	@Override
	public boolean equalTo(Value other) {
		if(other instanceof INumber num)
			return Math.abs(value - num.getDouble()) <= DOUBLE_PRECISION_EPSILON;
		
		return false;
	}
	
	public Value castInto(CastingType type) {
		if(type == CastingType.INTEGER)
			return IntegerValue.get((int)value);
		else if(type == CastingType.FLOAT)
			return new FloatValue((float) value);
		else if(type == CastingType.DOUBLE)
			return this;
		
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
	
	public String typeName() { return "double"; }

	@Override
	public int getInteger() {
		return (int) value;
	}

	@Override
	public float getFloat() {
		return (float) value;
	}
	
	@Override
	public double getDouble() {
		return value;
	}
	
	public Object toJavaObject() { return value; }
	public Class<?> toJavaClass(Object selfObject) { return double.class; }
	
	@Override
	public NumberType numberType() {
		return NumberType.DOUBLE;
	}

	@Override
	public boolean isInteger() {
		return false;
	}
	
}
