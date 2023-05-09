package holo.interpreter.nodes.statements;

import holo.errors.AssertionError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record SingleAssertNode(Node node, Sequence sequence) implements Node {
	
	public String toString() {
		return "assert " + node;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		Value value = rt.register(node.interpret(parentContext, interpreter, rt), node.sequence());
		if(rt.shouldReturn()) return rt;
		
		if(!value.isTrue())
			return rt.failure(new AssertionError(node.toString(), sequence));
		
		return rt.success(BooleanValue.TRUE);
	}
	
}
