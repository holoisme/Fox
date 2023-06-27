package holo.interpreter.nodes.var;

import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record VarAccessNode(String varName, Sequence sequence) implements Node {
	
	public String toString() {
		return varName.toString();
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value value = parentContext.get(varName);
		
		if(value == null)
			throw new TError(new NoSuchVariableError(varName, sequence));
		
		return value;
	}
	
}