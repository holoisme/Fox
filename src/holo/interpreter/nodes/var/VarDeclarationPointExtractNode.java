package holo.interpreter.nodes.var;

import holo.errors.AlreadyExistingVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.VarDeclaration;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarDeclarationPointExtractNode(String[] varNames, Node expression, Sequence sequence) implements Node, VarDeclaration {
	
	public String toString() {
		return "var {" + ReflectionUtils.toString(varNames) + "} from " + expression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value object = expression.interpret(parentContext, interpreter);
		
		for(String varName:varNames) {
			if(parentContext.contains(varName))
				throw new TError(new AlreadyExistingVariableError(varName, sequence));
			
			parentContext.setToThis(varName, object.pointGet(varName, sequence));
		}
		
		return Value.UNDEFINED;
	}
	
}
