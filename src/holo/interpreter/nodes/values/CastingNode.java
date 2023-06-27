package holo.interpreter.nodes.values;

import holo.errors.IllegalCastingError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.CastingType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record CastingNode(CastingType type, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return "("+type+") " + expression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value expressionValue = expression.interpret(parentContext, interpreter);
		
		Value castedValue = expressionValue.castInto(type);
		if(castedValue == null)
			throw new TError(new IllegalCastingError(expressionValue, type, sequence));
		
		return castedValue;
	}
	
}