package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record VarAssignmentNode(Node access, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return access + " = " + expression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value expressionValue = expression.interpret(parentContext, interpreter);
		
		if(access instanceof VarAccessNode van) {
			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
			if(contextThatDefinedVar == null)
				throw new TError(new NoSuchVariableError(van.varName(), van.sequence()));
			contextThatDefinedVar.setToThis(van.varName(), expressionValue);
			
			return expressionValue;
		} else if(access instanceof VarPointAccessNode vpa) {
			Value hostValue = vpa.access().interpret(parentContext, interpreter);
			
			Value returnedValue = hostValue.pointSet(vpa.varName(), expressionValue);
			if(returnedValue == null)
				throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
			
			return returnedValue;
		} else if(access instanceof VarArrayAccessNode vpa) {
			Value hostValue = vpa.access().interpret(parentContext, interpreter);
			Value index = vpa.index().interpret(parentContext, interpreter);
			
			Value returnedValue = hostValue.arraySet(index, expressionValue);
			if(returnedValue == null)
				throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
			
			return returnedValue;
		}
		
		throw new TError(new CannotAccessError(access.getClass().getSimpleName(), access.sequence()));
	}
	
}