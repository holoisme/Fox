package holo.interpreter.nodes.values;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.primitives.IntegerValue;
import holo.lang.lexer.Sequence;

public record IntegerNode(int value, Sequence sequence) implements Node {
	
	public String toString() {
		return value + "";
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		return onGoingRuntime.buffer(new IntegerValue(value));
	}
	
}
