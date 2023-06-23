package holo.interpreter.values.functions;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.helpers.SingleDefinitionArgument;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallable;

public abstract class BaseFunctionValue implements Value, ICallable {
	
	protected final SingleDefinitionArgument[] definingArguments;
	
	public BaseFunctionValue(SingleDefinitionArgument[] arguments) {
		this.definingArguments = arguments;
	}
	
	public abstract RuntimeResult call(Value host, Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime, Value... args);

	@Override
	public int numberOfArguments() {
		return definingArguments.length;
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
