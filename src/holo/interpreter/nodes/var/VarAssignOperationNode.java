package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.errors.IllegalOperationError;
import holo.errors.NoSuchVariableError;
import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.IntegerValue;
import holo.lang.lexer.Sequence;

public record VarAssignOperationNode(Node access, BinaryOperationType operation, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return access + " " + operation + " " + expression;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value expressionValue = onGoingRuntime.register(expression.interpret(parentContext, interpreter, onGoingRuntime), expression.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		if(operation == BinaryOperationType.DIVIDE &&
			expressionValue.equalTo(IntegerValue.ZERO)) {
			return onGoingRuntime.failure(new RuntimeError("Division by zero", expression.sequence()));
		}
		
		if(access instanceof VarAccessNode van) {
			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
			if(contextThatDefinedVar == null)
				return onGoingRuntime.failure(new NoSuchVariableError(van.varName(), van.sequence()));
			
			Value currentValue = contextThatDefinedVar.get(van.varName());
			Value result = currentValue.binaryOperation(operation, expressionValue);
			
			if(result == null)
				return onGoingRuntime.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			
			contextThatDefinedVar.setToThis(van.varName(), result);
			
			return onGoingRuntime.buffer(result);
		} else if(access instanceof VarPointAccessNode || access instanceof VarArrayAccessNode) {
			Value hostValue = onGoingRuntime.register(access.interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
			
			if(access instanceof VarPointAccessNode vpa) {
				Value currentValue = hostValue.pointGet(vpa.varName());
				if(currentValue == null)
					return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
				
				Value result = currentValue.binaryOperation(operation, expressionValue);
				
				if(result == null)
					return onGoingRuntime.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
				
				return onGoingRuntime.buffer(result);
			} else if(access instanceof VarArrayAccessNode vpa) {
				Value index = onGoingRuntime.register(vpa.index().interpret(parentContext, interpreter, onGoingRuntime), vpa.access().sequence());
				if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
				
				Value currentValue = hostValue.arrayGet(index);
				if(currentValue == null)
					return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
				
				Value result = currentValue.binaryOperation(operation, expressionValue);
				
				if(result == null)
					return onGoingRuntime.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
				
				return onGoingRuntime.buffer(result);
			}
		}
		
		return onGoingRuntime.failure(new CannotAccessError(access.getClass().getSimpleName(), access.sequence()));
	}
	
}