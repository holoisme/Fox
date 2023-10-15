package holo.interpreter.nodes.var;

import holo.errors.AlreadyExistingVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.VarDeclaration;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.numbers.IntegerValue;
import holo.lang.lexer.Sequence;

public record VarDeclarationArrayExtractNode(String[] varNames, Node expression, Sequence sequence) implements Node, VarDeclaration {
	
	public String toString() {
		return "var {" + ReflectionUtils.toString(varNames) + "} from " + expression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value object = expression.interpret(parentContext, interpreter);
		
		int index = 0;
		for(String varName:varNames) {
			if(parentContext.authorizeNewVar(varName))
				throw new TError(new AlreadyExistingVariableError(varName, sequence));
			
			parentContext.setToThis(varName, object.arrayGet(IntegerValue.get(index), sequence));
			index++;
		}
		
		return Value.UNDEFINED;
	}
	
}
