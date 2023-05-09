package holo.interpreter.nodes.var;

import holo.errors.CannotAccessError;
import holo.errors.NoSuchVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarAssignmentNode(Node access, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return access + " = " + expression;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value expressionValue = onGoingRuntime.register(expression.interpret(parentContext, interpreter, onGoingRuntime), expression.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		if(access instanceof VarAccessNode van) {
			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
			if(contextThatDefinedVar == null)
				return onGoingRuntime.failure(new NoSuchVariableError(van.varName(), van.sequence()));
			contextThatDefinedVar.setToThis(van.varName(), expressionValue);
			
			return onGoingRuntime.buffer(expressionValue);
		} else if(access instanceof VarPointAccessNode vpa) {
			Value hostValue = onGoingRuntime.register(vpa.access().interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			Value returnedValue = hostValue.pointSet(vpa.varName(), expressionValue);
			if(returnedValue == null)
				return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
			
			return onGoingRuntime.buffer(returnedValue);
		} else if(access instanceof VarArrayAccessNode vpa) {
			Value hostValue = onGoingRuntime.register(vpa.access().interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			Value index = onGoingRuntime.register(vpa.index().interpret(parentContext, interpreter, onGoingRuntime), vpa.access().sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			Value returnedValue = hostValue.arraySet(index, expressionValue);
			if(returnedValue == null)
				return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
			
			return onGoingRuntime.buffer(returnedValue);
		}
		
		return onGoingRuntime.failure(new CannotAccessError(access.getClass().getSimpleName(), access.sequence()));
	}
	
}