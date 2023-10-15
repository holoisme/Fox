package holo.interpreter.values.portal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import holo.errors.RuntimeError;
import holo.errors.portal.NoSuchMethodError;
import holo.interpreter.Interpreter;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallHandler;
import holo.interpreter.values.primitives.StringValue;
import holo.lang.lexer.Sequence;

public class FoxPortalWrapper implements Value, ICallHandler {
	
	private Object value;
	
	public FoxPortalWrapper(Object value) {
		this.value = value;
	}
	
	@Override
	public String typeName() {
		if(value instanceof FoxPortal)
			return ((FoxPortal) value).typeName();
		return value.getClass().getSimpleName();
	}
	
	@Override
	public Value typeOf() {
		if(value instanceof FoxPortal)
			return new StringValue( ((FoxPortal) value).typeName() );
		return new StringValue( value.getClass().getSimpleName() );
	}

	@Override
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		if(other instanceof FoxPortalWrapper w)
			return value.equals(w.value);
		return false;
	}

	@Override
	public Value pointGet(String key, Sequence sequence) {
		try {
			Field field = value.getClass().getField(key);
			if(field == null || field.isAnnotationPresent(FoxIgnore.class))
				return null;
			return Value.convertJavaToFoxValue(field.get(value));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Value callMethod(String name, Value[] args, NamedValue[] optionalArguments, Interpreter interpreter, Sequence sequence) {
		Object[] convertedArgs = new Object[args.length];
		Class<?>[] argsClasses = new Class<?>[args.length];
		
		for(int i = 0; i < args.length; i++) {
			convertedArgs[i] = args[i].toJavaObject();
			argsClasses[i] = args[i].toJavaClass(convertedArgs[i]);
		}
		
		try {
			Method method = value.getClass().getMethod(name, argsClasses);
			if(method == null || method.isAnnotationPresent(FoxIgnore.class))
				throw new TError(new NoSuchMethodError(name, argsClasses, typeName(), null));
			
			Object returnedObject = method.invoke(value, convertedArgs);
			
			return Value.convertJavaToFoxValue(returnedObject);
		} catch (Exception e) { throw new TError(new RuntimeError(e.getMessage(), null)); }
	}

	@Override
	public Value pointSet(String key, Value v, Sequence sequence) {
		try {
			Field field = value.getClass().getField(key);
			if(field == null || field.isAnnotationPresent(FoxIgnore.class))
				return null;
			field.set(value, v.toJavaObject());
			return v;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) { e.printStackTrace(); }
		return null;
	}
	
	public Object toJavaObject() { return value; }
	
	public String toString() { return value.toString(); }
	
}
