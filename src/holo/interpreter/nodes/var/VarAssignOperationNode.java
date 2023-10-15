package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.errors.IllegalOperationError;
import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarAssignOperationNode(Node access, BinaryOperationType operation, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return access + " " + operation + "= " + expression;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final Value expressionValue = expression.interpret(parentContext, interpreter);
		
		if(access instanceof VarAccessNode van) {
			final Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
			if(contextThatDefinedVar == null)
				throw new TError(new NoSuchVariableError(van.varName(), van.sequence()));
			
			final Value currentValue = contextThatDefinedVar.get(van.varName());
			final Value result = currentValue.binaryOperation(operation, expressionValue, interpreter, sequence);
			
			if(result == null)
				throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
			
			contextThatDefinedVar.setToThis(van.varName(), result);
			
			return result;
		} else if(access instanceof VarPointAccessNode vpa) {
			final Value hostValue = vpa.access().interpret(parentContext, interpreter);
			final Value result = hostValue.pointGet(vpa.varName(), sequence).binaryOperation(operation, expressionValue, interpreter, sequence);
			hostValue.pointSet(vpa.varName(), result, sequence);
			
			return result;
		} else if(access instanceof VarArrayAccessNode vaa) {
			final Value hostValue = vaa.access().interpret(parentContext, interpreter);
			final Value index = vaa.index().interpret(parentContext, interpreter);
			final Value result = hostValue.arrayGet(index, sequence).binaryOperation(operation, expressionValue, interpreter, sequence);
			hostValue.arraySet(index, result, sequence);
			
			return result;
		}
		
//		else if(access instanceof VarPointAccessNode || access instanceof VarArrayAccessNode) {
//			final Value hostValue = access.interpret(parentContext, interpreter);
//			System.out.println("HEre? " + hostValue);
//			if(access instanceof VarPointAccessNode vpa) {
//				final Value currentValue = hostValue.pointGet(vpa.varName());
//				if(currentValue == null)
//					throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
//				
//				final Value result = currentValue.binaryOperation(operation, expressionValue, interpreter);
//				
//				if(result == null)
//					throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
//				
//				return result;
//			} else if(access instanceof VarArrayAccessNode vpa) {
//				final Value index = vpa.index().interpret(parentContext, interpreter);
//				
//				final Value currentValue = hostValue.arrayGet(index);
//				if(currentValue == null)
//					throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
//				
//				final Value result = currentValue.binaryOperation(operation, expressionValue, interpreter);
//				
//				if(result == null)
//					throw new TError(new IllegalOperationError(operation.toString(), currentValue.typeName(), sequence));
//				
//				return result;
//			}
//		}
		
		throw new TError(new CannotAccessError(access.getClass().getSimpleName(), access.sequence()));
	}
	
}