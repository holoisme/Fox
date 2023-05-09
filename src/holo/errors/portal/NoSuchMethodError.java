package holo.errors.portal;

import java.util.Arrays;

import holo.errors.RuntimeError;
import holo.lang.lexer.Sequence;

public class NoSuchMethodError extends RuntimeError {

	public NoSuchMethodError(String name, Class<?>[] classes, String typeName, Sequence sequence) {
		super("No such method", "Cannot find " + name + "("+Arrays.toString(classes)+") method in " + typeName, sequence);
	}

}
