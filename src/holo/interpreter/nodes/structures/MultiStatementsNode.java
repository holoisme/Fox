package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record MultiStatementsNode(Node[] statements, Sequence sequence) implements Node {
	
	public String toString() {
		String body = "";
		for(Node n:statements)
			body += n + "\n";
		return "{\n" + body.indent(4) + "}";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Context context = new SimpleContext(parentContext.getName() + " child", parentContext);
		
		for(Node statementNode:statements())
			statementNode.interpret(context, interpreter);
		
		return Value.NULL;
	}
	
	public Value interpretTransparently(Context parentContext, Interpreter interpreter) {
		for(Node statementNode:statements())
			statementNode.interpret(parentContext, interpreter);
		
		return Value.NULL;
	}
	
}