package holo.interpreter.values.interfaces;

import holo.interpreter.Interpreter;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface ICallHandler {
	public Value callMethod(String name, Value[] args, NamedValue[] optionalArguments, Interpreter interpreter, Sequence sequence);
}
