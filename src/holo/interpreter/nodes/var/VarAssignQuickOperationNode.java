package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.errors.IllegalOperationError;
import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record VarAssignQuickOperationNode(Node access, QuickOperationType operation, Sequence sequence) implements Node {
	
	public String toString() {
		return access + " " + operation;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		
		if(access instanceof VarAccessNode van) {
			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
			if(contextThatDefinedVar == null)
				throw new TError(new NoSuchVariableError(van.varName(), van.sequence()));
			
			Value currentValue = contextThatDefinedVar.get(van.varName());
			Value result = currentValue.quickOperation(operation);
			
			if(result == null)
				throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			
			contextThatDefinedVar.setToThis(van.varName(), result);
			
			return result;
		} else if(access instanceof VarPointAccessNode vpa) {
			Value hostValue = vpa.access().interpret(parentContext, interpreter);
			
			Value currentValue = hostValue.pointGet(vpa.varName());
			if(currentValue == null)
				throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
			
			Value result = currentValue.quickOperation(operation);
			
			if(result == null)
				throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			
			hostValue.pointSet(vpa.varName(), result);
			
			return result;
		} else if(access instanceof VarArrayAccessNode vpa) {
			Value hostValue = vpa.access().interpret(parentContext, interpreter);
			Value index = vpa.index().interpret(parentContext, interpreter);
			
			Value currentValue = hostValue.arrayGet(index);
			if(currentValue == null)
				throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
			
			Value result = currentValue.quickOperation(operation);
			
			if(result == null)
				throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			hostValue.arraySet(index, result);
			
			return result;
		}
		
		throw new TError(new CannotAccessError(access.getClass().getSimpleName(), access.sequence()));
	}
	
}