package holo.interpreter.nodes.statements;

import holo.errors.AssertionError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record AssertNode(Node condition, Sequence sequence) implements Node {

	public String toString() {
		return "assert " + condition;
	}
	
	@Override
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value value = onGoingRuntime.register(condition.interpret(parentContext, interpreter, onGoingRuntime), condition.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		if(!value.isTrue())
			return onGoingRuntime.failure(new AssertionError("Condition " + condition.toString() + " should be true", condition.sequence()));
		
		return onGoingRuntime.success(value);
	}

}
