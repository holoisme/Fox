package holo.interpreter.values.primitives;

import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IInstanciable;
import holo.lang.lexer.Sequence;

public class ErrorClassValue implements Value, IInstanciable {
	
	public static final ErrorClassValue INSTANCE = new ErrorClassValue();
	
	private ErrorClassValue() {}
	
	@Override
	public Value createInstance(Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments) {
		return new RuntimeError(args[0].toString(), sequence);
	}

	@Override
	public String typeName() {
		return "Error class";
	}
	
	@Override
	public Value typeOf() { return new StringValue("error_class"); }

	@Override
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		return this == other;
	}

}
