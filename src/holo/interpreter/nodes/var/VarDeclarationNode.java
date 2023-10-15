package holo.interpreter.nodes.var;

import holo.errors.AlreadyExistingVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.VarDeclaration;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarDeclarationNode(String varName, Node expression, Sequence sequence) implements Node, VarDeclaration {
	
	public String toString() {
		return "var " + varName + " = " + expression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		if(parentContext.contains(varName))
			throw new TError(new AlreadyExistingVariableError(varName, sequence));
		
		final Value value = expression == null ? Value.NULL : expression.interpret(parentContext, interpreter);
		
		parentContext.setToThis(varName, value);
		
		return value;
	}
	
}
