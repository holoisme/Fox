package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record OptionalChainingNode(Node access, String varName, Sequence sequence) implements Node {
	
	public String toString() {
		return access + "?." + varName.toString();
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value hostValue = access.interpret(parentContext, interpreter);
		
		if(hostValue == Value.NULL)
			return Value.NULL;
		
		Value pointGetValue = hostValue.pointGet(varName);
		if(pointGetValue == null)
			throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
		
		return pointGetValue;
	}
	
}
