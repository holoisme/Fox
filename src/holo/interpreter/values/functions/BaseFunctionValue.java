package holo.interpreter.values.functions;

import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallable;

public abstract class BaseFunctionValue implements Value, ICallable {
	
	protected final ObligatoryDefinitionArgument[] regularArguments;
	protected final OptionalDefinitionArgument[] optionalArguments;
	
	public BaseFunctionValue(ObligatoryDefinitionArgument[] regularArguments, OptionalDefinitionArgument[] optionalArguments) {
		this.regularArguments = regularArguments;
		this.optionalArguments = optionalArguments;
	}
	
//	public abstract RuntimeResult call(Value host, Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime, Value... args);

	@Override
	public int numberOfArguments() {
		return regularArguments.length;
	}

	@Override
	public String typeName() {
		return "function";
	}

	@Override
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		return this == other;
	}

	@Override
	public Value pointGet(String key) {
		return null;
	}

	@Override
	public Value pointSet(String key, Value value) {
		return null;
	}

	@Override
	public Value arrayGet(Value key) {
		return null;
	}

	@Override
	public Value arraySet(Value key, Value value) {
		return null;
	}

}
