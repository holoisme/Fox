package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record OptionalChainingNode(Node access, String varName, Sequence sequence) implements Node {
	
	public String toString() {
		return access + "?." + varName.toString();
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value hostValue = onGoingRuntime.register(access.interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		if(hostValue == Value.NULL)
			return onGoingRuntime.buffer(Value.NULL);
		
		Value pointGetValue = hostValue.pointGet(varName);
		if(pointGetValue == null)
			return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
		
		return onGoingRuntime.buffer(pointGetValue);
	}
	
}
