package holo.interpreter.values.portal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IInstanciable;

public class ClassInstanciator implements Value, IInstanciable {
	
	private Class<?> cl;
	
	public ClassInstanciator(Class<?> cl) {
		this.cl = cl;
	}

	@Override
	public RuntimeResult createInstance(Interpreter interpreter, RuntimeResult onGoing, Value... args) {
		Object[] convertedArgs = new Object[args.length];
		Class<?>[] argsClasses = new Class<?>[args.length];
		
		System.out.println("-------");
		for(int i = 0; i < args.length; i++) {
			convertedArgs[i] = args[i].toJavaObject();
			argsClasses[i] = args[i].toJavaClass(convertedArgs[i]);
			System.out.println(argsClasses[i].getSimpleName());
		}
		System.out.println("-------");
		
		System.out.println("Instanciate " + cl.getSimpleName());
		try {
			Constructor<?> constructor = cl.getConstructor(argsClasses);
			System.out.println("Args: " + Arrays.toString(convertedArgs));
			System.out.println(constructor);
			
			Object instance = constructor.newInstance(convertedArgs);
			System.out.println("letsgo !");
			System.out.println("\n");
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
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		return equals(other);
	}

	public Value pointGet(String key) {
		return null;
	}

	public Value pointSet(String key, Value value) {
		return null;
	}

	public Value arrayGet(Value key) {
		return null;
	}

	public Value arraySet(Value key, Value value) {
		return null;
	}
	
}
