package holo.interpreter.values;

import java.util.ArrayList;
import java.util.List;

import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.portal.FoxPortal;
import holo.interpreter.values.portal.FoxPortalWrapper;
import holo.interpreter.values.primitives.BooleanValue;
import holo.interpreter.values.primitives.CharValue;
import holo.interpreter.values.primitives.DoubleValue;
import holo.interpreter.values.primitives.FloatValue;
import holo.interpreter.values.primitives.IntegerValue;
import holo.interpreter.values.primitives.ListValue;
import holo.interpreter.values.primitives.NullValue;
import holo.interpreter.values.primitives.StringValue;

public interface Value {
	
	public static final NullValue NULL = new NullValue();
	
	public String typeName();
	
	public boolean isTrue();
	public boolean equalTo(Value other);
	
	public Value pointGet(String key);
	public Value pointSet(String key, Value value);
	public Value arrayGet(Value key);
	public Value arraySet(Value key, Value value);
	
	public default Value unaryOperation(UnaryOperationType operation) { return operation == UnaryOperationType.NOT ? BooleanValue.get(!isTrue()) : null; }
	
	public default Value binaryOperation(BinaryOperationType operation, Value right) {
		if(operation == BinaryOperationType.AND)
			return BooleanValue.get(isTrue() && right.isTrue());
		else if(operation == BinaryOperationType.OR)
			return BooleanValue.get(isTrue() || right.isTrue());
		else if(operation == BinaryOperationType.PLUS && right instanceof StringValue sv)
			return new StringValue(toString() + sv.getValue());
		else if(operation == BinaryOperationType.DOUBLE_EQUALS)
			return BooleanValue.get(equalTo(right));
		else if(operation == BinaryOperationType.NOT_EQUAL)
			return BooleanValue.get(!equalTo(right));
		
		return null;
	}
	
	public default Value quickOperation(QuickOperationType operation) { return null; }
	
	public default Value castInto(CastingType type) {
		if(type == CastingType.STRING)
			return this instanceof StringValue ? this : new StringValue(toString());
		else if(type == CastingType.BOOLEAN)
			return BooleanValue.get(isTrue());
		
		return null;
	}
	
	public default Object toJavaObject() { return this; }
	public default Class<?> toJavaClass(Object selfObject) { return selfObject.getClass(); }
	
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
