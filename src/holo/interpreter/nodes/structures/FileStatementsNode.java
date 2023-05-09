package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
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
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		for(Node statementNode:statements()) {
			rt.register(statementNode.interpret(parentContext, interpreter, rt), parentContext.getName(), statementNode.sequence());
			if(rt.shouldReturn()) return rt;
		}
		
		return rt.success(Value.NULL);
	}
	
}