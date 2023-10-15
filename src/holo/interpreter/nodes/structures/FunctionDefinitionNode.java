package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.values.FunctionExpressionNode;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record FunctionDefinitionNode(String name, FunctionExpressionNode functionExpression, Sequence sequence) implements Node {
	
	public String toString() {
		return "function"+ (name==null?"":" " + name) + functionExpression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value value = functionExpression.interpret(parentContext, interpreter);
		
		parentContext.setToThis(name, value);
		
		return value;
	}
	
}