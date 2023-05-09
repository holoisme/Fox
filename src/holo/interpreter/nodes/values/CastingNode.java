package holo.interpreter.nodes.values;

import holo.errors.IllegalCastingError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.CastingType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record CastingNode(CastingType type, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return "("+type+") " + expression;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		Value expressionValue = rt.register(expression.interpret(parentContext, interpreter, rt), expression.sequence());
		if(rt.shouldReturn()) return rt;
		
		Value castedValue = expressionValue.castInto(type);
		if(castedValue == null)
			return rt.failure(new IllegalCastingError(expressionValue, type, sequence));
		
		return rt.success(castedValue);
	}
	
}