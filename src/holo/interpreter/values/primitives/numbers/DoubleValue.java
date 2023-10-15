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
		return Math.abs(value) > 0.0;
	}
	
	public Value unaryOperation(UnaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		return operation == UnaryOperationType.NEGATE ? new DoubleValue(-value) : Value.super.unaryOperation(operation, interpreter, sequence);
	}
	
	public Value binaryOperation(BinaryOperationType operation, Value right, Interpreter interpreter, Sequence sequence) {
		if(right instanceof INumber num) {
			if(num.numberType().ordinal() > INumber.NumberType.DOUBLE.ordinal())
				return num.handle(this, operation, interpreter, sequence);
			
			final double right_double = num.get().doubleValue();
			
			switch (operation) {
				case PLUS:
					return new DoubleValue(value + right_double);
				case MINUS:
					return new DoubleValue(value - right_double);
				case MULTIPLY:
					return new DoubleValue(value * right_double);
				case DIVIDE:
					if(right_double == 0)
						throw new TError(new DivisionByZeroError(sequence));
					return new DoubleValue(value / right_double);
				case EXPONANT:
					return new DoubleValue(Math.pow(value, right_double));
				case MODULO:
					if(right_double == 0)
						throw new TError(new DivisionByZeroError(sequence));
					return new DoubleValue(value % right_double);
				
				case DOUBLE_EQUALS:
					return BooleanValue.get(value == right_double);
				case NOT_EQUAL:
					return BooleanValue.get(value != right_double);
				case LESS_THAN:
					return BooleanValue.get(value < right_double);
				case GREATER_THAN:
					return BooleanValue.get(value > right_double);
				case LESS_OR_EQUAL:
					return BooleanValue.get(value <= right_double);
				case GREATER_OR_EQUAL:
					return BooleanValue.get(value >= right_double);
					
				case LOGICAL_SHIFT_LEFT:
				case LOGICAL_SHIFT_RIGHT:
				case LOGICAL_UNSIGNED_SHIFT_RIGHT:
				case LOGICAL_XOR:
				case LOGICAL_AND:
				case LOGICAL_OR:
					throw new TError(new IllegalOperationError(operation.toString(), "double", sequence));
			}
			
		}
		
		return Value.super.binaryOperation(operation, right, interpreter, sequence);
	}
	
	public Value quickOperation(QuickOperationType operation, Interpreter interpreter, Sequence sequence) {
		if(operation == QuickOperationType.PLUS_PLUS)
			return new DoubleValue(value + 1);
		else if(operation == QuickOperationType.MINUS_MINUS)
			return new DoubleValue(value - 1);
		
		return Value.super.quickOperation(operation, interpreter, sequence);
	}
	
	@Override
	public boolean equalTo(Value other) {
		if(other instanceof INumber num)
			return Math.abs(value - num.get().doubleValue()) <= DOUBLE_PRECISION_EPSILON;
		
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
			return new FloatValue((float)value);
		case DOUBLE:
			return this;
		case BOOLEAN:
			return BooleanValue.get(value != 0);
		case STRING:
			return new StringValue(String.valueOf(value));
		}
		
		return Value.super.castInto(type, interpreter, sequence);
	}
	
	@Override
	public String typeName() { return "double"; }
	
	@Override
	public Value typeOf() { return new StringValue("double"); }

	@Override
	public int getInteger() { return (int) value; }
	
	@Override
	public Number get() { return value; }

	@Override
	public boolean isInteger() { return false; }
	
	@Override
	public Object toJavaObject() { return value; }
	
	@Override
	public Class<?> toJavaClass(Object selfObject) { return double.class; }
	
	@Override
	public NumberType numberType() { return NumberType.DOUBLE; }
	
	@Override
	public Value handle(INumber left, BinaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		final double left_double = left.get().doubleValue();
		
		switch (operation) {
			case PLUS:
				return new DoubleValue(left_double + value);
			case MINUS:
				return new DoubleValue(left_double - value);
			case MULTIPLY:
				return new DoubleValue(left_double * value);
			case DIVIDE:
				if(value == 0)
					throw new TError(new DivisionByZeroError(sequence));
				return new DoubleValue(left_double / value);
			case EXPONANT:
				return new DoubleValue((double) Math.pow(left_double, value));
			case MODULO:
				if(value == 0)
					throw new TError(new DivisionByZeroError(sequence));
				return new DoubleValue(left_double % value);
			
			case DOUBLE_EQUALS:
				return BooleanValue.get(left_double == value);
			case NOT_EQUAL:
				return BooleanValue.get(left_double != value);
			case LESS_THAN:
				return BooleanValue.get(left_double < value);
			case GREATER_THAN:
				return BooleanValue.get(left_double > value);
			case LESS_OR_EQUAL:
				return BooleanValue.get(left_double <= value);
			case GREATER_OR_EQUAL:
				return BooleanValue.get(left_double >= value);
				
			case LOGICAL_SHIFT_LEFT:
			case LOGICAL_SHIFT_RIGHT:
			case LOGICAL_UNSIGNED_SHIFT_RIGHT:
			case LOGICAL_XOR:
			case LOGICAL_AND:
			case LOGICAL_OR:
				throw new TError(new IllegalOperationError(operation.toString(), "double", sequence));
		}
		
		throw new TError(new IllegalOperationError(operation.toString(), "double", sequence));
	}
	
}
