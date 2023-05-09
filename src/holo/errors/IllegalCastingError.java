package holo.errors;

import holo.interpreter.types.CastingType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public class IllegalCastingError extends RuntimeError {

	public IllegalCastingError(Value value, CastingType castingType, Sequence sequence) {
		super("Illegal Casting", "Cannot cast " + value + " (" + value.typeName() + ") into " + castingType.toString(), sequence);
	}

}
