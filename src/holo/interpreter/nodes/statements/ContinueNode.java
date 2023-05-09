package holo.interpreter.nodes.statements;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.lang.lexer.Sequence;

public record ContinueNode(Sequence sequence) implements Node {
	
	public String toString() {
		return "continue";
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		return onGoingRuntime.encounterContinue();
	}
	
}