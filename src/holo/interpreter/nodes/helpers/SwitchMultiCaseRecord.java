package holo.interpreter.nodes.helpers;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.Sequenced;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record SwitchMultiCaseRecord(Node[] equality, Node expression, Sequence sequence) implements Sequenced, SwitchCase {

	public String toString() {
		return "case " + ReflectionUtils.toString(equality) + ": " + expression.toString();
	}
	
	@Override
	public RuntimeResult doesMatch(Value value, Context context, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		for(Node e:equality) {
			Value evaluated = onGoingRuntime.register(e.interpret(context, interpreter, onGoingRuntime), e.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			if(value.equalTo(evaluated))
				return onGoingRuntime.buffer(BooleanValue.TRUE);
		}
		
		return onGoingRuntime.buffer(BooleanValue.FALSE);
	}

}
