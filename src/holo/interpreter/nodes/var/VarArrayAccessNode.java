package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarArrayAccessNode(Node access, Node index, Sequence sequence) implements Node {
	
	public String toString() {
		return access + "[" + index + "]";
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
//		RuntimeResult rt = new RuntimeResult();
		
		Value hostValue = onGoingRuntime.register(access.interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		Value indexValue = onGoingRuntime.register(index.interpret(parentContext, interpreter, onGoingRuntime), index.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		Value arrayGetValue = hostValue.arrayGet(indexValue);
		if(arrayGetValue == null)
			return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
		
		return onGoingRuntime.buffer(arrayGetValue);
	}
	
}