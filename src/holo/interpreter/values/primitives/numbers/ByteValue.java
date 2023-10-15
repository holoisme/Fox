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

public class ByteValue implements Value, INumber {
	
	private final byte value;
	
	public ByteValue(byte value) {
		this.value = value;
	}
	
	public byte getValue() { return value; }
	
	public String toString() { return value+""; }
	
	@Override
	public Value unaryOperation(UnaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		return operation == UnaryOperationType.NEGATE ? new ByteValue((byte) -value) : operation == UnaryOperationType.LOGICAL_NOT ? new ByteValue((byte) ~value) : Value.super.unaryOperation(operation, interpreter, sequence);
	}
	
	@Override
	public Value binaryOperation(BinaryOperationType operation, Value right, Interpreter interpreter, Sequence sequence) {
		if(right instanceof INumber num) {
			if(num.numberType().ordinal() > INumber.NumberType.BYTE.ordinal())
				return num.handle(this, operation, interpreter, sequence);
			
			final int right_int = num.getInteger();
			
			switch (operation) {
				case PLUS:
					return IntegerValue.get(value + right_int);
				case MINUS:
					return IntegerValue.get(value - right_int);
				case MULTIPLY:
					return IntegerValue.get(value * right_int);
				case DIVIDE:
					if(right_int == 0)
						throw new TError(new DivisionByZeroError(sequence));
					return IntegerValue.get(value / right_int);
				case EXPONANT:
					return IntegerValue.get((int) Math.pow(value, right_int));
				case MODULO:
					if(right_int == 0)
						throw new TError(new DivisionByZeroError(sequence));
					return IntegerValue.get(value % right_int);
				
				case DOUBLE_EQUALS:
					return BooleanValue.get(value == right_int);
				case NOT_EQUAL:
					return BooleanValue.get(value != right_int);
				case LESS_THAN:
					return BooleanValue.get(value < right_int);
				case GREATER_THAN:
					return BooleanValue.get(value > right_int);
				case LESS_OR_EQUAL:
					return BooleanValue.get(value <= right_int);
				case GREATER_OR_EQUAL:
					return BooleanValue.get(value >= right_int);
					
				case LOGICAL_SHIFT_LEFT:
					return IntegerValue.get(value << right_int);
				case LOGICAL_SHIFT_RIGHT:
					return IntegerValue.get(value >> right_int);
				case LOGICAL_UNSIGNED_SHIFT_RIGHT:
					return IntegerValue.get(value >>> right_int);
				case LOGICAL_XOR:
					return IntegerValue.get(value ^ right_int);
				case LOGICAL_AND:
					return IntegerValue.get(value & right_int);
				case LOGICAL_OR:
					return IntegerValue.get(value | right_int);
			}
			
		}
		
		return Value.super.binaryOperation(operation, right, interpreter, sequence);
	}
	
	@Override
	public Value quickOperation(QuickOperationType operation, Interpreter interpreter, Sequence sequence) {
		if(operation == QuickOperationType.PLUS_PLUS)
			return new ByteValue((byte) (value + 1));
		else if(operation == QuickOperationType.MINUS_MINUS)
			return new ByteValue((byte) (value - 1));
		
		return Value.super.quickOperation(operation, interpreter, sequence);
	}
	
	@Override
	public Value castInto(CastingType type, Interpreter interpreter, Sequence sequence) {
		switch(type) {
		case INTEGER:
			return IntegerValue.get(value);
		case BYTE:
			return this;
		case SHORT:
			return new ShortValue((short)value);
		case CHARACTER:
			return new CharValue((char)value);
		case LONG:
			return new LongValue(value);
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
		if(other instanceof ByteValue iv)
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
	public String typeName() { return "byte"; }
	
	@Override
	public Value typeOf() { return new StringValue("byte"); }
	
	@Override
	public int getInteger() { return value; }
	
	@Override
	public Number get() { return value; }
	
	@Override
	public Object toJavaObject() { return value; }
	
	@Override
	public Class<?> toJavaClass(Object selfObject) { return byte.class; }

	@Override
	public NumberType numberType() { return NumberType.BYTE; }

	@Override
	public boolean isInteger() { return true; }
	
	@Override
	public Value handle(INumber left, BinaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		final int left_int = left.getInteger();
		
		switch (operation) {
			case PLUS:
				return IntegerValue.get(left_int + value);
			case MINUS:
				return IntegerValue.get(left_int - value);
			case MULTIPLY:
				return IntegerValue.get(left_int * value);
			case DIVIDE:
				if(value == 0)
					throw new TError(new DivisionByZeroError(sequence));
				return IntegerValue.get(left_int / value);
			case EXPONANT:
				return IntegerValue.get((int) Math.pow(left_int, value));
			case MODULO:
				if(value == 0)
					throw new TError(new DivisionByZeroError(sequence));
				return IntegerValue.get(left_int % value);
			
			case DOUBLE_EQUALS:
				return BooleanValue.get(left_int == value);
			case NOT_EQUAL:
				return BooleanValue.get(left_int != value);
			case LESS_THAN:
				return BooleanValue.get(left_int < value);
			case GREATER_THAN:
				return BooleanValue.get(left_int > value);
			case LESS_OR_EQUAL:
				return BooleanValue.get(left_int <= value);
			case GREATER_OR_EQUAL:
				return BooleanValue.get(left_int >= value);
				
			case LOGICAL_SHIFT_LEFT:
				return IntegerValue.get(left_int << value);
			case LOGICAL_SHIFT_RIGHT:
				return IntegerValue.get(left_int >> value);
			case LOGICAL_UNSIGNED_SHIFT_RIGHT:
				return IntegerValue.get(left_int >>> value);
			case LOGICAL_XOR:
				return IntegerValue.get(left_int ^ value);
			case LOGICAL_AND:
				return IntegerValue.get(left_int & value);
			case LOGICAL_OR:
				return IntegerValue.get(left_int | value);
		}
		
		throw new TError(new IllegalOperationError(operation.toString(), "byte", sequence));
	}
	
}
