package holo.interpreter.nodes.values;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record DefaultValueNode(Value value, Sequence sequence) implements Node {
	
	public String toString() {
		return value.toString();
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		return onGoingRuntime.buffer(value);
	}
	
}