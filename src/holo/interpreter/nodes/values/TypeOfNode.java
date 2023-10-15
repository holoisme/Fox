package holo.interpreter.nodes.values;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record TypeOfNode(Node expression, Sequence sequence) implements Node {
	
	@Override
	public String toString() {
		return "typeof " + expression.toString();
	}

	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		return expression.interpret(parentContext, interpreter).typeOf();
	}

}
