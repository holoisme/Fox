package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.SwitchCase;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record SwitchNode(Node valueNode, SwitchCase[] cases, Node catchNode, Sequence sequence) implements Node {
	
	public String toString() {
		return "switch("+valueNode+") {\n" + ReflectionUtils.toString(cases, "\n") + "\n" + (catchNode != null ? "catch: " + catchNode + "\n" : "") + "}";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value value = valueNode.interpret(parentContext, interpreter);
		
		for(SwitchCase c : cases)
			if(c.doesMatch(value, parentContext, interpreter))
				return c.expression().interpret(parentContext, interpreter);
		
		return catchNode == null ? Value.UNDEFINED : catchNode.interpret(parentContext, interpreter);
	}
	
}