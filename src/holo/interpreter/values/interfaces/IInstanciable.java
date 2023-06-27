package holo.interpreter.values.interfaces;

import holo.interpreter.Interpreter;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public interface IInstanciable {
	
	public Value createInstance(Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments);
	
}
