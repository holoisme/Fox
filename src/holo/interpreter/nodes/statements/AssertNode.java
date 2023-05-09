package holo.interpreter.nodes.statements;

import java.util.Arrays;

import holo.errors.AssertionError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record AssertNode(Node[] conditions, Sequence sequence) implements Node {

	public String toString() {
		return "assert " + Arrays.toString(conditions);
	}
	
	@Override
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		for(Node condition:conditions) {
			Value value = onGoingRuntime.register(condition.interpret(parentContext, interpreter, onGoingRuntime), condition.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			if(!value.isTrue())
				return onGoingRuntime.failure(new AssertionError("Condition " + condition.toString() + " should be true", condition.sequence()));
		}
		
		return onGoingRuntime.success(Value.NULL);
	}

}
