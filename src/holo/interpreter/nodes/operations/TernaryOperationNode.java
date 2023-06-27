package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record TernaryOperationNode(Node condition, Node ifTrue, Node ifFalse, Sequence sequence) implements Node {
	
	public String toString() {
		return "{" + condition + " ? " + ifTrue + " : " + ifFalse+"}";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		return condition.interpret(parentContext, interpreter).isTrue() ?
				ifTrue.interpret(parentContext, interpreter) :
				ifFalse.interpret(parentContext, interpreter);
	}
	
}