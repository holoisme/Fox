package holo.interpreter.nodes.statements;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TContinue;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record ContinueNode(Sequence sequence) implements Node {
	
	public String toString() {
		return "continue";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		throw TContinue.CONTINUE;
	}
	
}