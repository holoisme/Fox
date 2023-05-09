package holo.interpreter.nodes.var;

import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarAccessNode(String varName, Sequence sequence) implements Node {
	
	public String toString() {
		return varName.toString();
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
//		RuntimeResult rt = new RuntimeResult();
		
		Value value = parentContext.get(varName);
		if(value == null)
			return onGoingRuntime.failure(new NoSuchVariableError(varName, sequence));
		
		return onGoingRuntime.buffer(value);
	}
	
}