package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.errors.IllegalOperationError;
import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarAssignQuickOperationNode(Node access, QuickOperationType operation, Sequence sequence) implements Node {
	
	public String toString() {
		return access + " " + operation;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		
		if(access instanceof VarAccessNode van) {
			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
			if(contextThatDefinedVar == null)
				return onGoingRuntime.failure(new NoSuchVariableError(van.varName(), van.sequence()));
			
			Value currentValue = contextThatDefinedVar.get(van.varName());
			Value result = currentValue.quickOperation(operation);
			
			if(result == null)
				return onGoingRuntime.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			
			contextThatDefinedVar.setToThis(van.varName(), result);
			
			return onGoingRuntime.buffer(result);
		} else if(access instanceof VarPointAccessNode vpa) {
			Value hostValue = onGoingRuntime.register(vpa.access().interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			
			Value currentValue = hostValue.pointGet(vpa.varName());
			if(currentValue == null)
				return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
			
			Value result = currentValue.quickOperation(operation);
			
			if(result == null)
				return onGoingRuntime.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			
			hostValue.pointSet(vpa.varName(), result);
			
			return onGoingRuntime.buffer(result);
		} else if(access instanceof VarArrayAccessNode vpa) {
			Value hostValue = onGoingRuntime.register(vpa.access().interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			Value index = onGoingRuntime.register(vpa.index().interpret(parentContext, interpreter, onGoingRuntime), vpa.access().sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			Value currentValue = hostValue.arrayGet(index);
			if(currentValue == null)
				return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
			
			Value result = currentValue.quickOperation(operation);
			
			if(result == null)
				return onGoingRuntime.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			hostValue.arraySet(index, result);
			
			return onGoingRuntime.buffer(result);
		}
		
		return onGoingRuntime.failure(new CannotAccessError(access.getClass().getSimpleName(), access.sequence()));
	}
	
}