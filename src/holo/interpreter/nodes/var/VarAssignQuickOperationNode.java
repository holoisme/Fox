package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarAssignQuickOperationNode(Node access, QuickOperationType operation, Sequence sequence) implements Node {
	
	public String toString() {
		return access + "" + operation;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		
		if(access instanceof VarAccessNode van) {
			final Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
			if(contextThatDefinedVar == null)
				throw new TError(new NoSuchVariableError(van.varName(), van.sequence()));
			
			final Value currentValue = contextThatDefinedVar.get(van.varName());
			final Value result = currentValue.quickOperation(operation, interpreter, sequence);
			
			contextThatDefinedVar.setToThis(van.varName(), result);
			
			return currentValue;
		} else if(access instanceof VarPointAccessNode vpa) {
			final Value hostValue = vpa.access().interpret(parentContext, interpreter);
			
			final Value currentValue = hostValue.pointGet(vpa.varName(), sequence);
			
			final Value result = currentValue.quickOperation(operation, interpreter, sequence);
			
			hostValue.pointSet(vpa.varName(), result, sequence);
			
			return currentValue;
		} else if(access instanceof VarArrayAccessNode vpa) {
			final Value hostValue = vpa.access().interpret(parentContext, interpreter);
			final Value index = vpa.index().interpret(parentContext, interpreter);
			
			final Value currentValue = hostValue.arrayGet(index, sequence);
			
			final Value result = currentValue.quickOperation(operation, interpreter, sequence);
			
			hostValue.arraySet(index, result, sequence);
			
			return currentValue;
		}
		
		throw new TError(new CannotAccessError(access.getClass().getSimpleName(), access.sequence()));
	}
	
}