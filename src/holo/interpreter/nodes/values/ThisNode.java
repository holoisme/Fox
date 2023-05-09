package holo.interpreter.nodes.values;

import holo.errors.NoThisError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.lang.lexer.Sequence;

public record ThisNode(Sequence sequence) implements Node {
	
	public String toString() {
		return "this";
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Context firstThisableParentContext = parentContext.getFirstThisableParent();
		if(firstThisableParentContext == null)
			return onGoingRuntime.failure(new NoThisError(sequence));
		
		return onGoingRuntime.buffer(firstThisableParentContext.thisValue());
	}
	
}