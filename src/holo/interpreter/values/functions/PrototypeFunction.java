package holo.interpreter.values.functions;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.helpers.SingleDefinitionArgument;
import holo.interpreter.values.Value;

public class PrototypeFunction extends BaseFunctionValue {

	public PrototypeFunction(SingleDefinitionArgument[] arguments) {
		super(arguments);
	}

	public RuntimeResult call(Value host, Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime, Value... args) {
		return null;
	}

}
