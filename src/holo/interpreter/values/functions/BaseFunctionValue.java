package holo.interpreter.values.functions;

import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallable;
import holo.interpreter.values.primitives.StringValue;

public abstract class BaseFunctionValue implements Value, ICallable {
	
	protected final ObligatoryDefinitionArgument[] regularArguments;
	protected final OptionalDefinitionArgument[] optionalArguments;
	
	public BaseFunctionValue(ObligatoryDefinitionArgument[] regularArguments, OptionalDefinitionArgument[] optionalArguments) {
		this.regularArguments = regularArguments;
		this.optionalArguments = optionalArguments;
	}
	
	@Override
	public int numberOfArguments() {
		return regularArguments.length;
	}

	@Override
	public String typeName() {
		return "function";
	}
	
	@Override
	public Value typeOf() { return new StringValue("function"); }

	@Override
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		return this == other;
	}

}
