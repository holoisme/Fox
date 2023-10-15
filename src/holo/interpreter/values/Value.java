package holo.interpreter.values;

import java.util.ArrayList;
import java.util.List;

import holo.errors.CannotAccessError;
import holo.errors.IllegalCastingError;
import holo.errors.IllegalOperationError;
import holo.interpreter.Interpreter;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.portal.FoxPortal;
import holo.interpreter.values.portal.FoxPortalWrapper;
import holo.interpreter.values.primitives.BooleanValue;
import holo.interpreter.values.primitives.ListValue;
import holo.interpreter.values.primitives.NullValue;
import holo.interpreter.values.primitives.StringValue;
import holo.interpreter.values.primitives.UndefinedValue;
import holo.interpreter.values.primitives.numbers.CharValue;
import holo.interpreter.values.primitives.numbers.DoubleValue;
import holo.interpreter.values.primitives.numbers.FloatValue;
import holo.interpreter.values.primitives.numbers.IntegerValue;
import holo.lang.lexer.Sequence;

public interface Value {
	
	public static final NullValue NULL = NullValue.NULL;
	public static final UndefinedValue UNDEFINED = UndefinedValue.UNDEFINED;
	
	public Value typeOf();
	public default String typeName() { return typeOf().toString(); }
	
	public boolean isTrue();
	public boolean equalTo(Value other);
	
	public default String toString(Interpreter interpreter, Sequence sequence) {
		return toString();
	}
	
	public default Value pointGet(String key, Sequence sequence) {
		throw new TError(new CannotAccessError(typeName(), sequence));
	}
	
	public default Value pointSet(String key, Value value, Sequence sequence) {
		throw new TError(new CannotAccessError(typeName(), sequence));
	}
	
	public default Value arrayGet(Value key, Sequence sequence) {
		throw new TError(new CannotAccessError(typeName(), sequence));
	}
	
	public default Value arraySet(Value key, Value value, Sequence sequence) {
		throw new TError(new CannotAccessError(typeName(), sequence));
	}
	
	public default Value unaryOperation(UnaryOperationType operation, Interpreter interpreter, Sequence sequence) {
		throw new TError(new IllegalOperationError(operation.name(), typeName(), sequence));
	}
	
	public default Value binaryOperation(BinaryOperationType operation, Value right, Interpreter interpreter, Sequence sequence) {
		if(operation == BinaryOperationType.PLUS && right instanceof StringValue sv)
			return new StringValue(toString(interpreter, sequence) + sv.getValue());
		else if(operation == BinaryOperationType.DOUBLE_EQUALS)
			return BooleanValue.get(equalTo(right));
		else if(operation == BinaryOperationType.NOT_EQUAL)
			return BooleanValue.get(!equalTo(right));
		
		throw new TError(new IllegalOperationError(operation.name(), typeName(), sequence));
	}
	
	public default Value quickOperation(QuickOperationType operation, Interpreter interpreter, Sequence sequence) {
		throw new TError(new IllegalOperationError(operation.name(), typeName(), sequence));
	}
	
	public default Value castInto(CastingType type, Interpreter interpreter, Sequence sequence) {
		if(type == CastingType.STRING)
			return new StringValue(toString(interpreter, sequence));
		else if(type == CastingType.BOOLEAN)
			return BooleanValue.get(isTrue());
//		if(type == CastingType.STRING)
//			return this instanceof StringValue ? this : new StringValue(toString());
		
		throw new TError(new IllegalCastingError(this, type, sequence));
	}
	
	public default Object toJavaObject() { return this; }
	public default Class<?> toJavaClass(Object selfObject) { return selfObject.getClass(); }
	public default boolean nullOrUndefined() { return false; }
	
	public static Value convertJavaToFoxValue(Object obj) {
		if(obj == null) return Value.NULL;
		else if(obj instanceof Integer v) return IntegerValue.get(v);
		else if(obj instanceof Float v) return new FloatValue(v);
		else if(obj instanceof Double v) return new DoubleValue(v);
		else if(obj instanceof Character v) return new CharValue(v);
		else if(obj instanceof Boolean v) return BooleanValue.get(v);
		else if(obj instanceof String v) return new StringValue(v);
		else if(obj instanceof Value v) return v;
		else if(obj instanceof FoxPortal v) return new FoxPortalWrapper(v);
		else if(obj instanceof List<?> list) {
			List<Value> elements = new ArrayList<>();
			for(Object l:list)
				elements.add(convertJavaToFoxValue(l));
			return new ListValue(elements);
		}
		
		return new FoxPortalWrapper(obj);
	}
	
}
