package holo.interpreter.values.functions;

import holo.interpreter.Interpreter;
import holo.interpreter.LambdaExecutor;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.values.Value;

public class BuiltInFunctionValue extends BaseFunctionValue {

	private String name;
	private LambdaExecutor executor;
	
	public BuiltInFunctionValue(String name, LambdaExecutor executor, ObligatoryDefinitionArgument[] regularArguments, OptionalDefinitionArgument[] optionalArguments) {
		super(regularArguments, optionalArguments);
		this.name = name;
		this.executor = executor;
	}
	
	public BuiltInFunctionValue(String name, LambdaExecutor executor, String... arguments) {
		super(toArrayOfObligatoryArguments(arguments), new OptionalDefinitionArgument[0]);
		this.name = name;
		this.executor = executor;
	}
	
	public BuiltInFunctionValue(String name, LambdaExecutor executor) {
		super(new ObligatoryDefinitionArgument[0], new OptionalDefinitionArgument[0]);
		this.name = name;
		this.executor = executor;
	}

	public Value call(Value host, Context parentContext, Interpreter interpreter, Value[] args, NamedValue[] optionalArguments) {
		Context callContext = new SimpleContext("built-in-function " + name, null);
		for(int i = 0; i < regularArguments.length; i++)
			callContext.setToThis(regularArguments[i].name(), args[i]);
		
		Value value = executor.call(host, callContext, interpreter, args);
		return value == null ? Value.NULL : value;
	}
	
	public String getName() { return name; }
	
	private static ObligatoryDefinitionArgument[] toArrayOfObligatoryArguments(String... args) {
		if(args == null)
			return new ObligatoryDefinitionArgument[0];
		ObligatoryDefinitionArgument[] result = new ObligatoryDefinitionArgument[args.length];
		for(int i = 0; i < result.length; i++)
			result[i] = new ObligatoryDefinitionArgument(args[i], null);
		return result;
	}

}
