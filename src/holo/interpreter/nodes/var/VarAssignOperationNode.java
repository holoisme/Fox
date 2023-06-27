package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.errors.IllegalOperationError;
import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record VarAssignOperationNode(Node access, BinaryOperationType operation, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return access + " " + operation + " " + expression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value expressionValue = expression.interpret(parentContext, interpreter);
		
		if(access instanceof VarAccessNode van) {
			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
			if(contextThatDefinedVar == null)
				throw new TError(new NoSuchVariableError(van.varName(), van.sequence()));
			
			Value currentValue = contextThatDefinedVar.get(van.varName());
			Value result = currentValue.binaryOperation(operation, expressionValue);
			
			if(result == null)
				throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			
			contextThatDefinedVar.setToThis(van.varName(), result);
			
			return result;
		} else if(access instanceof VarPointAccessNode || access instanceof VarArrayAccessNode) {
			Value hostValue = access.interpret(parentContext, interpreter);
			
			if(access instanceof VarPointAccessNode vpa) {
				Value currentValue = hostValue.pointGet(vpa.varName());
				if(currentValue == null)
					throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
				
				Value result = currentValue.binaryOperation(operation, expressionValue);
				
				if(result == null)
					throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
				
				return result;
			} else if(access instanceof VarArrayAccessNode vpa) {
				Value index = vpa.index().interpret(parentContext, interpreter);
				
				Value currentValue = hostValue.arrayGet(index);
				if(currentValue == null)
					throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
				
				Value result = currentValue.binaryOperation(operation, expressionValue);
				
				if(result == null)
					throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
				
				return result;
			}
		}
		
		throw new TError(new CannotAccessError(access.getClass().getSimpleName(), access.sequence()));
	}
	
}