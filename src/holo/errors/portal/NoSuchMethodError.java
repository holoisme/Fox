package holo.errors.portal;

import holo.errors.RuntimeError;
import holo.interpreter.nodes.ReflectionUtils;
import holo.lang.lexer.Sequence;

public class NoSuchMethodError extends RuntimeError {

	public NoSuchMethodError(String name, Class<?>[] classes, String typeName, Sequence sequence) {
		super("No such method", "Cannot find " + name + "("+ReflectionUtils.toString(classes)+") method in " + typeName, sequence);
	}

}
