package holo.interpreter.values.primitives.numbers;

import holo.errors.DivisionByZeroError;
import holo.errors.IllegalOperationError;
import holo.interpreter.Interpreter;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.primitives.BooleanValue;
import holo.interpreter.values.primitives.StringValue;
import holo.lang.lexer.Sequence;

public class CharValue implements Value, INumber {
	
	private final char value;
	
	public CharValue(char value) {
		this.value = value;
	}
	
	public char getValue() { return value; }
	
	public String toString() { return Character.toString(value); }
	
	@Override
	public Value binaryOperation(BinaryOperationType operation, Value right, Interpreter interpreter, Sequence sequence) {
		if(right instanceof INumber num) {
			if(num.numberType().ordinal() > INumber.NumberType.CHAR.ordinal())
				return num.handle(this, operation, interpreter, sequence);
			
			final int right_int = num.get().intValue();
			
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
			return new CharValue((char) (value + 1));
		else if(operation == QuickOperationType.MINUS_MINUS)
			return new CharValue((char) (value - 1));
		
		return Value.super.quickOperation(operation, interpreter, sequence);
	}
	
	@Override
	public Value castInto(CastingType type, Interpreter interpreter, Sequence sequence) {
		switch(type) {
		case INTEGER:
			return IntegerValue.get(value);
		case BYTE:
			return new ByteValue((byte)value);
		case SHORT:
			return new ShortValue((short)value);
		case CHARACTER:
			return this;
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
	public String typeName() { return "char"; }
	
	@Override
	public Value typeOf() { return new StringValue("char"); }

	@Override
	public int getInteger() { return value; }
	
	@Override
	public Number get() { return (int)value; }

	@Override
	public boolean isInteger() { return false; }
	
	@Override
	public Object toJavaObject() { return value; }
	
	@Override
	public Class<?> toJavaClass(Object selfObject) { return char.class; }

	@Override
	public NumberType numberType() { return NumberType.CHAR; }

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
		
		throw new TError(new IllegalOperationError(operation.toString(), "char", sequence));
	}
	
}
