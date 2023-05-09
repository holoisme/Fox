package holo.interpreter.nodes;

import java.util.Arrays;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.helpers.DefinitionArgument;
import holo.interpreter.nodes.helpers.SingleDefinitionArgument;
import holo.interpreter.values.functions.FunctionValue;
import holo.lang.lexer.Sequence;

public record FunctionExpressionNode(DefinitionArgument[] arguments, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "("+Arrays.toString(arguments)+") -> " + body;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		SingleDefinitionArgument[] singleArguments = new SingleDefinitionArgument[arguments.length];
		for(int i = 0; i < arguments.length; i++)
			singleArguments[i] = arguments[i].argument();
		
		return rt.success(new FunctionValue(singleArguments, parentContext, body));
	}
	
	public int numberOfArguments() { return arguments.length; }
	
}