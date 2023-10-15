package holo.interpreter.nodes.values;

import holo.errors.NoThisError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record ThisNode(Sequence sequence) implements Node {
	
	public String toString() {
		return "this";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Context firstThisableParentContext = parentContext.getFirstThisableParent();
		
		if(firstThisableParentContext == null)
			throw new TError(new NoThisError(sequence));
		
		return firstThisableParentContext.thisValue();
	}
	
}