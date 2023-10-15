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

public class LongValue implements Value, INumber {
	
	private final long value;
	
	public LongValue(long value) {
		this.value = value;
	}
	
	public long getValue() { return value; }
	
	public String toString() { return value+""; }
	
	@Override
	public Value unaryOperation(UnaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		return operation == UnaryOperationType.NEGATE ? new LongValue(-value) : operation == UnaryOperationType.LOGICAL_NOT ? new LongValue(~value) : Value.super.unaryOperation(operation, interpreter, sequence);
	}
	
	@Override
	public Value binaryOperation(BinaryOperationType operation, Value right, Interpreter interpreter, Sequence sequence) {
		if(right instanceof INumber num) {
			if(num.numberType().ordinal() > INumber.NumberType.LONG.ordinal())
				return num.handle(this, operation, interpreter, sequence);
			
			final long right_long = num.get().longValue();
			
			switch (operation) {
				case PLUS:
					return new LongValue(value + right_long);
				case MINUS:
					return new LongValue(value - right_long);
				case MULTIPLY:
					return new LongValue(value * right_long);
				case DIVIDE:
					if(right_long == 0)
						throw new TError(new DivisionByZeroError(sequence));
					return new LongValue(value / right_long);
				case EXPONANT:
					return new LongValue((int) Math.pow(value, right_long));
				case MODULO:
					if(right_long == 0)
						throw new TError(new DivisionByZeroError(sequence));
					return new LongValue(value % right_long);
				
				case DOUBLE_EQUALS:
					return BooleanValue.get(value == right_long);
				case NOT_EQUAL:
					return BooleanValue.get(value != right_long);
				case LESS_THAN:
					return BooleanValue.get(value < right_long);
				case GREATER_THAN:
					return BooleanValue.get(value > right_long);
				case LESS_OR_EQUAL:
					return BooleanValue.get(value <= right_long);
				case GREATER_OR_EQUAL:
					return BooleanValue.get(value >= right_long);
					
				case LOGICAL_SHIFT_LEFT:
					return new LongValue(value << right_long);
				case LOGICAL_SHIFT_RIGHT:
					return new LongValue(value >> right_long);
				case LOGICAL_UNSIGNED_SHIFT_RIGHT:
					return new LongValue(value >>> right_long);
				case LOGICAL_XOR:
					return new LongValue(value ^ right_long);
				case LOGICAL_AND:
					return new LongValue(value & right_long);
				case LOGICAL_OR:
					return new LongValue(value | right_long);
			}
			
		}
		
		return Value.super.binaryOperation(operation, right, interpreter, sequence);
	}
	
	@Override
	public Value quickOperation(QuickOperationType operation, Interpreter interpreter, Sequence sequence) {
		if(operation == QuickOperationType.PLUS_PLUS)
			return new LongValue(value + 1);
		else if(operation == QuickOperationType.MINUS_MINUS)
			return new LongValue(value - 1);
		
		return Value.super.quickOperation(operation, interpreter, sequence);
	}
	
	@Override
	public Value castInto(CastingType type, Interpreter interpreter, Sequence sequence) {
		switch(type) {
		case INTEGER:
			return IntegerValue.get((int) value);
		case BYTE:
			return new ByteValue((byte)value);
		case SHORT:
			return new ShortValue((short)value);
		case CHARACTER:
			return new CharValue((char)value);
		case LONG:
			return this;
		case FLOAT:
			return new FloatValue(value);
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
	public boolean isTrue() { return value != 0; }
	
	@Override
	public boolean equalTo(Value other) {
		if(other instanceof LongValue iv)
			return value == iv.value;
		else if(other instanceof FloatValue fv)
			return Math.abs(value - fv.getValue()) < FloatValue.FLOAT_PRECISION_EPSILON;
		else if(other instanceof DoubleValue dv)
			return Math.abs(value - dv.getValue()) < DoubleValue.DOUBLE_PRECISION_EPSILON;
		else if(other instanceof INumber num)
			return value == num.getInteger();
		
		return false;
	}
	
	@Override
	public String typeName() { return "long"; }
	
	@Override
	public Value typeOf() { return new StringValue("long"); }
	
	@Override
	public int getInteger() { return (int) value; }
	
	@Override
	public Number get() { return value; }
	
	@Override
	public Object toJavaObject() { return value; }
	
	@Override
	public Class<?> toJavaClass(Object selfObject) { return long.class; }

	@Override
	public NumberType numberType() { return NumberType.LONG; }

	@Override
	public boolean isInteger() { return true; }
	
	@Override
	public Value handle(INumber left, BinaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		final long left_long = left.get().longValue();
		
		switch (operation) {
			case PLUS:
				return new LongValue(left_long + value);
			case MINUS:
				return new LongValue(left_long - value);
			case MULTIPLY:
				return new LongValue(left_long * value);
			case DIVIDE:
				if(value == 0)
					throw new TError(new DivisionByZeroError(sequence));
				return new LongValue(left_long / value);
			case EXPONANT:
				return new LongValue((long) Math.pow(left_long, value));
			case MODULO:
				if(value == 0)
					throw new TError(new DivisionByZeroError(sequence));
				return new LongValue(left_long % value);
			
			case DOUBLE_EQUALS:
				return BooleanValue.get(left_long == value);
			case NOT_EQUAL:
				return BooleanValue.get(left_long != value);
			case LESS_THAN:
				return BooleanValue.get(left_long < value);
			case GREATER_THAN:
				return BooleanValue.get(left_long > value);
			case LESS_OR_EQUAL:
				return BooleanValue.get(left_long <= value);
			case GREATER_OR_EQUAL:
				return BooleanValue.get(left_long >= value);
				
			case LOGICAL_SHIFT_LEFT:
				return new LongValue(left_long << value);
			case LOGICAL_SHIFT_RIGHT:
				return new LongValue(left_long >> value);
			case LOGICAL_UNSIGNED_SHIFT_RIGHT:
				return new LongValue(left_long >>> value);
			case LOGICAL_XOR:
				return new LongValue(left_long ^ value);
			case LOGICAL_AND:
				return new LongValue(left_long & value);
			case LOGICAL_OR:
				return new LongValue(left_long | value);
		}
		
		throw new TError(new IllegalOperationError(operation.toString(), "long", sequence));
	}
	
}
