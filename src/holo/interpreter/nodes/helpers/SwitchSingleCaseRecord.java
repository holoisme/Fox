package holo.interpreter.nodes.helpers;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.Sequenced;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record SwitchSingleCaseRecord(Node equality, Node expression, Sequence sequence) implements Sequenced, SwitchCase {

	public String toString() {
		return "case " + equality.toString() + ": " + expression.toString();
	}
	
	@Override
	public RuntimeResult doesMatch(Value value, Context context, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value evaluated = onGoingRuntime.register(equality.interpret(context, interpreter, onGoingRuntime), equality.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		return onGoingRuntime.buffer(BooleanValue.get(value.equalTo(evaluated)));
	}

}
