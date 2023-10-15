package holo.interpreter.nodes.statements;

import holo.errors.AssertionError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record MultiAssertNode(Node[] conditions, Sequence sequence) implements Node {

	public String toString() {
		return "assert " + ReflectionUtils.toString(conditions);
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		for(Node condition:conditions)
			if(!condition.interpret(parentContext, interpreter).isTrue())
				throw new TError(new AssertionError("Condition " + condition.toString() + " should be true", condition.sequence()));
		
		return Value.UNDEFINED;
	}

}
