package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.FunctionExpressionNode;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record FunctionDefinitionNode(String name, FunctionExpressionNode functionExpression, Sequence sequence) implements Node {
	
	public String toString() {
		return "function"+ (name==null?"":" " + name) + functionExpression;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		Value value = rt.register(functionExpression.interpret(parentContext, interpreter, rt), functionExpression.sequence());
		if(rt.shouldReturn()) return rt;
		
		parentContext.setToThis(name, value);
		
		return rt.success(value);
	}
	
}