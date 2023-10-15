package holo.interpreter.nodes.statements;

import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record ThrowNode(Node expression, Sequence sequence) implements Node {

	public String toString() {
		return "throw " + expression;
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value error = expression.interpret(parentContext, interpreter);
		throw new TError((RuntimeError) error);
	}

}
