package holo.interpreter.nodes.var;

import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarAccessNode(String varName, Sequence sequence) implements Node {
	
	public String toString() {
		return varName.toString();
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value value = parentContext.get(varName);
		
		if(value == null)
			throw new TError(new NoSuchVariableError(varName, sequence));
		
		return value;
	}
	
}