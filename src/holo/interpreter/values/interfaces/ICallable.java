package holo.interpreter.values.interfaces;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface ICallable {
	
	public Value call(Value host, Context parentContext, Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments);
	public int numberOfArguments();
	
}
