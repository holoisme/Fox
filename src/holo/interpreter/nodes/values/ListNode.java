package holo.interpreter.nodes.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.ListValue;
import holo.lang.lexer.Sequence;

public record ListNode(Node[] nodes, Sequence sequence) implements Node {
	
	public String toString() {
		return Arrays.toString(nodes);
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final List<Value> elements = new ArrayList<>();
		
		for(Node elementNode: nodes)
			elements.add(elementNode.interpret(parentContext, interpreter));
		
		return new ListValue(elements);
	}
	
}