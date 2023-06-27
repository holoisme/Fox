package holo.interpreter.nodes.helpers;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.Sequenced;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record SwitchSingleCaseRecord(Node equality, Node expression, Sequence sequence) implements Sequenced, SwitchCase {

	public String toString() {
		return "case " + equality.toString() + ": " + expression.toString();
	}
	
	@Override
	public boolean doesMatch(Value value, Context context, Interpreter interpreter) {
		return value.equalTo(equality.interpret(context, interpreter));
	}

}
