package holo.interpreter.nodes;

import java.util.Arrays;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.helpers.ExtractFromObjectInstance;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record ExtractFromObjectNode(ExtractFromObjectInstance[] instances, Node fromExpression, Sequence sequence) implements Node {

	public String toString() {
		return "extract " + Arrays.toString(instances) + " from " + fromExpression;
	}

	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		// TODO Auto-generated method stub
		return null;
	}

}
