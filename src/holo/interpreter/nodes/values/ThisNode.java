package holo.interpreter.nodes.values;

import holo.errors.NoThisError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record ThisNode(Sequence sequence) implements Node {
	
	public String toString() {
		return "this";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Context firstThisableParentContext = parentContext.getFirstThisableParent();
		
		if(firstThisableParentContext == null)
			throw new TError(new NoThisError(sequence));
		
		return firstThisableParentContext.thisValue();
	}
	
}