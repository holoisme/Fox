package holo.interpreter.values.portal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import holo.errors.RuntimeError;
import holo.errors.portal.NoSuchMethodError;
import holo.interpreter.RuntimeResult;
import holo.interpreter.values.Value;

public class FoxPortalWrapper implements Value {
	
	private Object value;
	
	public FoxPortalWrapper(Object value) {
		this.value = value;
	}
	
	@Override
	public String typeName() {
		if(value instanceof FoxPortal)
			return ((FoxPortal) value).typeName();
		return "portal";
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
	public Value pointGet(String key) {
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
	
	public RuntimeResult callMethod(RuntimeResult onGoing, String name, Value... args) {
		Object[] convertedArgs = new Object[args.length];
		Class<?>[] argsClasses = new Class<?>[args.length];
		
		for(int i = 0; i < args.length; i++) {
			convertedArgs[i] = args[i].toJavaObject();
			argsClasses[i] = args[i].toJavaClass(convertedArgs[i]);
		}
		
		try {
			Method method = value.getClass().getMethod(name, argsClasses);
			if(method == null || method.isAnnotationPresent(FoxIgnore.class))
				return onGoing.failure(new NoSuchMethodError(name, argsClasses, typeName(), null));
			
			Object returnedObject = method.invoke(value, convertedArgs);
			
			return onGoing.success(Value.convertJavaToFoxValue(returnedObject));
		} catch (Exception e) {
			return onGoing.failure(new RuntimeError(e.getMessage(), null));
		}
	}

	@Override
	public Value pointSet(String key, Value v) {
		try {
			Field field = value.getClass().getField(key);
			if(field == null || field.isAnnotationPresent(FoxIgnore.class))
				return null;
			System.out.println(field);
			field.set(value, v.toJavaObject());
			return v;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) { e.printStackTrace(); }
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
	
	public Object toJavaObject() { return value; }
	
	public String toString() { return value.toString(); }
	
}
