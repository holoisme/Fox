package holo.interpreter.values.portal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import holo.errors.RuntimeError;
import holo.errors.portal.NoSuchMethodError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallHandler;
import holo.interpreter.values.interfaces.IInstanciable;
import holo.lang.lexer.Sequence;

public class ClassInstanciator implements Value, IInstanciable, ICallHandler {
	
	private Class<?> cl;
	
	private Map<String, Field> staticFields;
	
	public ClassInstanciator(Class<?> cl) {
		this.cl = cl;
		
//		Field[] declaredFields = cl.getDeclaredFields();
//		staticFields = new HashMap<>();
//		for (Field field : declaredFields)
//		    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
//		    	staticFields.put(field.getName(), field);
//		    	
//		    }
	}

	@Override
	public RuntimeResult createInstance(Interpreter interpreter, RuntimeResult onGoing, Sequence sequence, Value... args) {
		Object[] convertedArgs = new Object[args.length];
		Class<?>[] argsClasses = new Class<?>[args.length];
		
		for(int i = 0; i < args.length; i++) {
			convertedArgs[i] = args[i].toJavaObject();
			argsClasses[i] = args[i].toJavaClass(convertedArgs[i]);
		}
		
		try {
			Constructor<?> constructor = cl.getConstructor(argsClasses);
			
			Object instance = constructor.newInstance(convertedArgs);
			return onGoing.buffer(Value.convertJavaToFoxValue(instance));
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return onGoing.failure(new RuntimeError(e.getMessage(), null));
		}
	}

	@Override
	public String typeName() {
		return cl.getSimpleName() + " class";
	}
	
	@Override
	public String toString() {
		return cl.getSimpleName() + " class";
	}

	@Override
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		return equals(other);
	}

	public Value pointGet(String key) {
		try {
			Field field = cl.getDeclaredField(key);
			if(!Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(FoxIgnore.class))
				return null;
			return Value.convertJavaToFoxValue(field.get(null));
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public Value pointSet(String key, Value v) {
		try {
			Field field = cl.getDeclaredField(key);
			if(field == null || !Modifier.isStatic(field.getModifiers()) || field.isAnnotationPresent(FoxIgnore.class))
				return null;
			field.set(null, v.toJavaObject());
			return v;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) { e.printStackTrace(); }
		
		return null;
	}

	public Value arrayGet(Value key) {
		return null;
	}

	public Value arraySet(Value key, Value value) {
		return null;
	}

	@Override
	public RuntimeResult callMethod(RuntimeResult onGoing, String name, Value... args) {
		Object[] convertedArgs = new Object[args.length];
		Class<?>[] argsClasses = new Class<?>[args.length];
		
		for(int i = 0; i < args.length; i++) {
			convertedArgs[i] = args[i].toJavaObject();
			argsClasses[i] = args[i].toJavaClass(convertedArgs[i]);
		}
		
		try {
			Method method = cl.getMethod(name, argsClasses);
			if(method == null || method.isAnnotationPresent(FoxIgnore.class))
				return onGoing.failure(new NoSuchMethodError(name, argsClasses, typeName(), null));
			
			Object returnedObject = method.invoke(null, convertedArgs);
			
			return onGoing.success(Value.convertJavaToFoxValue(returnedObject));
		} catch (Exception e) { return onGoing.failure(new RuntimeError(e.getMessage(), null)); }
	}
	
}
