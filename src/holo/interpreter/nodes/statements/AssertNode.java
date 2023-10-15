package holo.interpreter.nodes.statements;

import holo.errors.AssertionError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record AssertNode(Node condition, Sequence sequence) implements Node {

	public String toString() {
		return "assert " + condition;
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value value = condition.interpret(parentContext, interpreter);
		
		if(!value.isTrue())
			throw new TError(new AssertionError("Condition " + condition.toString() + " should be true", condition.sequence()));
		
		return value;
	}

}
