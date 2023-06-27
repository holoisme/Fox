package holo.interpreter.nodes.statements;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TReturn;

public record ReturnNode(Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return "return " + (expression!=null?expression:"");
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		throw new TReturn(expression == null ? Value.NULL : expression.interpret(parentContext, interpreter));
	}
	
}