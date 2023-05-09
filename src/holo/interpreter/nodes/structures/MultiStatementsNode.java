package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
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
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		Context context = new SimpleContext(parentContext.getName() + " child", parentContext);
		
		for(Node statementNode:statements()) {
			rt.register(statementNode.interpret(context, interpreter, rt), context.getName(), statementNode.sequence());
			if(rt.shouldReturn()) return rt;
		}
		
		return rt.success(Value.NULL);
	}
	
}