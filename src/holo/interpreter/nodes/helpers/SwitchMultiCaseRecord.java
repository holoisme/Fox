package holo.interpreter.nodes.helpers;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.Sequenced;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record SwitchMultiCaseRecord(Node[] equality, Node expression, Sequence sequence) implements Sequenced, SwitchCase {

	public String toString() {
		return "case " + ReflectionUtils.toString(equality) + ": " + expression.toString();
	}
	
	@Override
	public boolean doesMatch(Value value, Context context, Interpreter interpreter) {
		for(Node e:equality)
			if(value.equalTo(e.interpret(context, interpreter)))
				return true;
		
		return false;
	}

}
