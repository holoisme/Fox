package holo.interpreter.nodes.values;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.primitives.FloatValue;
import holo.lang.lexer.Sequence;

public record FloatNode(float value, Sequence sequence) implements Node {
	
	public String toString() {
		return value + "";
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		return onGoingRuntime.buffer(new FloatValue(value));
	}
	
}