package holo.interpreter.nodes.helpers;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;

public interface VarDeclaration {
	
	Node expression();
	Value interpret(Context parentContext, Interpreter interpreter);
	
}
