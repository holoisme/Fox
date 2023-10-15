package holo.interpreter.nodes.values;

import holo.errors.IllegalCastingError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.CastingType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record CastingNode(CastingType type, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return "("+type+") " + expression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value expressionValue = expression.interpret(parentContext, interpreter);
		
		final Value castedValue = expressionValue.castInto(type, interpreter, sequence);
		if(castedValue == null)
			throw new TError(new IllegalCastingError(expressionValue, type, sequence));
		
		return castedValue;
	}
	
}