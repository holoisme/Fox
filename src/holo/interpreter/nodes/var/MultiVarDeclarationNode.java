package holo.interpreter.nodes.var;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.VarDeclaration;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record MultiVarDeclarationNode(VarDeclaration[] declarations, Sequence sequence) implements Node {

	public Value interpret(Context parentContext, Interpreter interpreter) {
		for(VarDeclaration declaration:declarations)
			declaration.interpret(parentContext, interpreter);
		return Value.UNDEFINED;
	}

	@Override
	public String toString() {
		return ReflectionUtils.toString(declarations);
	}

}
