package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record NullishOperationNode(Node left, Node right, Sequence sequence) implements Node {
	
	public String toString() {
		return left+" ?? "+right;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value leftValue = onGoingRuntime.register(left.interpret(parentContext, interpreter, onGoingRuntime), left.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		if(leftValue.isTrue())
			return onGoingRuntime.buffer(leftValue);
		
		Value rightValue = onGoingRuntime.register(right.interpret(parentContext, interpreter, onGoingRuntime), right.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		return onGoingRuntime.buffer(rightValue);
	}
	
}