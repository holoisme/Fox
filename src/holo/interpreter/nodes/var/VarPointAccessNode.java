package holo.interpreter.nodes.var;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarPointAccessNode(Node access, String varName, Sequence sequence) implements Node {
	
	public String toString() {
		return access + "." + varName.toString();
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value hostValue = access.interpret(parentContext, interpreter);
		
		final Value pointGetValue = hostValue.pointGet(varName, sequence);
		
		return pointGetValue;
	}
	
}