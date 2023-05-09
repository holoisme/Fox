package holo.interpreter.values.functions;

import holo.interpreter.Interpreter;
import holo.interpreter.LambdaExecutor;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.helpers.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.SingleDefinitionArgument;
import holo.interpreter.values.Value;

public class BuiltInFunctionValue extends BaseFunctionValue {

	private String name;
	private LambdaExecutor executor;
	
	public BuiltInFunctionValue(String name, LambdaExecutor executor, SingleDefinitionArgument... arguments) {
		super(arguments);
		this.name = name;
		this.executor = executor;
	}
	
	public BuiltInFunctionValue(String name, LambdaExecutor executor, String... arguments) {
		super(toArrayOfObligatoryArguments(arguments));
		this.name = name;
		this.executor = executor;
	}
	
	public BuiltInFunctionValue(String name, LambdaExecutor executor) {
		super(new SingleDefinitionArgument[0]);
		this.name = name;
		this.executor = executor;
	}

	public RuntimeResult call(Value host, Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime, Value... args) {
		Context callContext = new SimpleContext("built-in-function " + name, null);
		for(int i = 0; i < definingArguments.length; i++)
			callContext.setToThis(definingArguments[i].name(), args[i]);
		
		Value value = onGoingRuntime.register(executor.call(host, callContext, interpreter, onGoingRuntime, args), null);
		if(onGoingRuntime.hasError()) return onGoingRuntime;
		
		return onGoingRuntime.buffer(value == null ? Value.NULL : value);
	}
	
	public String getName() { return name; }
	
	private static SingleDefinitionArgument[] toArrayOfObligatoryArguments(String... args) {
		if(args == null)
			return new SingleDefinitionArgument[0];
		SingleDefinitionArgument[] result = new SingleDefinitionArgument[args.length];
		for(int i = 0; i < result.length; i++)
			result[i] = new ObligatoryDefinitionArgument(args[i]);
		return result;
	}

}
