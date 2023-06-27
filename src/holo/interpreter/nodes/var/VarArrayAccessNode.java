package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record VarArrayAccessNode(Node access, Node index, Sequence sequence) implements Node {
	
	public String toString() {
		return access + "[" + index + "]";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value hostValue = access.interpret(parentContext, interpreter);
		Value indexValue = index.interpret(parentContext, interpreter);
		
		Value arrayGetValue = hostValue.arrayGet(indexValue);
		if(arrayGetValue == null)
			throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
		
		return arrayGetValue;
	}
	
}