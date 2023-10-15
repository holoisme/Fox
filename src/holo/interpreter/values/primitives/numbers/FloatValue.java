package holo.interpreter.values.primitives.numbers;

import holo.errors.DivisionByZeroError;
import holo.errors.IllegalOperationError;
import holo.interpreter.Interpreter;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.primitives.BooleanValue;
import holo.interpreter.values.primitives.StringValue;
import holo.lang.lexer.Sequence;

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
	
	public Value unaryOperation(UnaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		return operation == UnaryOperationType.NEGATE ? new FloatValue(-value) : Value.super.unaryOperation(operation, interpreter, sequence);
	}
	
	public Value binaryOperation(BinaryOperationType operation, Value right, Interpreter interpreter, Sequence sequence) {
		if(right instanceof INumber num) {
			if(num.numberType().ordinal() > INumber.NumberType.FLOAT.ordinal())
				return num.handle(this, operation, interpreter, sequence);
			
			final float right_float = num.get().floatValue();
			
			switch (operation) {
				case PLUS:
					return new FloatValue(value + right_float);
				case MINUS:
					return new FloatValue(value - right_float);
				case MULTIPLY:
					return new FloatValue(value * right_float);
				case DIVIDE:
					if(right_float == 0)
						throw new TError(new DivisionByZeroError(sequence));
					return new FloatValue(value / right_float);
				case EXPONANT:
					return new FloatValue((float) Math.pow(value, right_float));
				case MODULO:
					if(right_float == 0)
						throw new TError(new DivisionByZeroError(sequence));
					return new FloatValue(value % right_float);
				
				case DOUBLE_EQUALS:
					return BooleanValue.get(value == right_float);
				case NOT_EQUAL:
					return BooleanValue.get(value != right_float);
				case LESS_THAN:
					return BooleanValue.get(value < right_float);
				case GREATER_THAN:
					return BooleanValue.get(value > right_float);
				case LESS_OR_EQUAL:
					return BooleanValue.get(value <= right_float);
				case GREATER_OR_EQUAL:
					return BooleanValue.get(value >= right_float);
					
				case LOGICAL_SHIFT_LEFT:
				case LOGICAL_SHIFT_RIGHT:
				case LOGICAL_UNSIGNED_SHIFT_RIGHT:
				case LOGICAL_XOR:
				case LOGICAL_AND:
				case LOGICAL_OR:
					throw new TError(new IllegalOperationError(operation.toString(), "float", sequence));
			}
			
		}
		
		return Value.super.binaryOperation(operation, right, interpreter, sequence);
	}
	
	public Value quickOperation(QuickOperationType operation, Interpreter interpreter, Sequence sequence) {
		if(operation == QuickOperationType.PLUS_PLUS)
			return new FloatValue(value + 1);
		else if(operation == QuickOperationType.MINUS_MINUS)
			return new FloatValue(value - 1);
		
		return Value.super.quickOperation(operation, interpreter, sequence);
	}
	
	@Override
	public boolean equalTo(Value other) {
		if(other instanceof INumber num)
			return Math.abs(value - num.get().floatValue()) <= FLOAT_PRECISION_EPSILON;
		
		return false;
	}
	
	@Override
	public Value castInto(CastingType type, Interpreter interpreter, Sequence sequence) {
		switch(type) {
		case INTEGER:
			return IntegerValue.get((int)value);
		case BYTE:
			return new ByteValue((byte)value);
		case SHORT:
			return new ShortValue((short)value);
		case CHARACTER:
			return new CharValue((char)value);
		case LONG:
			return new LongValue((long) value);
		case FLOAT:
			return this;
		case DOUBLE:
			return new DoubleValue(value);
		case BOOLEAN:
			return BooleanValue.get(value != 0);
		case STRING:
			return new StringValue(String.valueOf(value));
		}
		
		return Value.super.castInto(type, interpreter, sequence);
	}
	
	@Override
	public String typeName() { return "float"; }

	@Override
	public Value typeOf() { return new StringValue("float"); }
	
	@Override
	public int getInteger() { return (int) value; }

	@Override
	public Number get() { return value; }
	
	@Override
	public Object toJavaObject() { return value; }
	
	@Override
	public Class<?> toJavaClass(Object selfObject) { return float.class; }
	
	@Override
	public NumberType numberType() { return NumberType.FLOAT; }
	
	@Override
	public boolean isInteger() { return false; }
	
	@Override
	public Value handle(INumber left, BinaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		final float left_float = left.get().floatValue();
		
		switch (operation) {
			case PLUS:
				return new FloatValue(left_float + value);
			case MINUS:
				return new FloatValue(left_float - value);
			case MULTIPLY:
				return new FloatValue(left_float * value);
			case DIVIDE:
				if(value == 0)
					throw new TError(new DivisionByZeroError(sequence));
				return new FloatValue(left_float / value);
			case EXPONANT:
				return new FloatValue((float) Math.pow(left_float, value));
			case MODULO:
				if(value == 0)
					throw new TError(new DivisionByZeroError(sequence));
				return new FloatValue(left_float % value);
			
			case DOUBLE_EQUALS:
				return BooleanValue.get(left_float == value);
			case NOT_EQUAL:
				return BooleanValue.get(left_float != value);
			case LESS_THAN:
				return BooleanValue.get(left_float < value);
			case GREATER_THAN:
				return BooleanValue.get(left_float > value);
			case LESS_OR_EQUAL:
				return BooleanValue.get(left_float <= value);
			case GREATER_OR_EQUAL:
				return BooleanValue.get(left_float >= value);
				
			case LOGICAL_SHIFT_LEFT:
			case LOGICAL_SHIFT_RIGHT:
			case LOGICAL_UNSIGNED_SHIFT_RIGHT:
			case LOGICAL_XOR:
			case LOGICAL_AND:
			case LOGICAL_OR:
				throw new TError(new IllegalOperationError(operation.toString(), "float", sequence));
		}
		
		throw new TError(new IllegalOperationError(operation.toString(), "float", sequence));
	}
	
}
