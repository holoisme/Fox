package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarPointAccessNode(Node access, String varName, Sequence sequence) implements Node {
	
	public String toString() {
		return access + "." + varName.toString();
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value hostValue = onGoingRuntime.register(access.interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		Value pointGetValue = hostValue.pointGet(varName);
		if(pointGetValue == null)
			return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
		
		return onGoingRuntime.buffer(pointGetValue);
	}
	
}