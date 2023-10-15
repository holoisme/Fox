package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record FileStatementsNode(Node[] statements, Sequence sequence) implements Node {
	
	public String toString() {
		String body = "";
		for(Node n:statements)
			body += n + "\n";
		return "{\n" + body.indent(4) + "}";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		for(Node statementNode:statements())
			statementNode.interpret(parentContext, interpreter);
		
		return Value.UNDEFINED;
	}
	
}