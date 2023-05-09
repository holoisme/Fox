package holo.interpreter.nodes.calls;

import java.util.Arrays;

import holo.errors.CannotAccessError;
import holo.errors.CannotCallError;
import holo.errors.IncorrectNumberOfArguments;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.var.VarPointAccessNode;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallable;
import holo.interpreter.values.portal.FoxPortalWrapper;
import holo.lang.lexer.Sequence;

public record CallNode(Node access, Node[] args, Sequence sequence) implements Node {
	
	public String toString() {
		return access.toString() + "("+Arrays.toString(args)+")";
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value hostValue = null;
		Value accessValue = null;
		
		Value[] argsValues = new Value[args.length];
		for(int i = 0; i < args.length; i++) {
			argsValues[i] = onGoingRuntime.register(args[i].interpret(parentContext, interpreter, onGoingRuntime), args[i].sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		
		if(access instanceof VarPointAccessNode pointAccess) {
			hostValue = onGoingRuntime.register(pointAccess.access().interpret(parentContext, interpreter, onGoingRuntime), pointAccess.access().sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			if(hostValue instanceof FoxPortalWrapper wrapper)
				return handleFoxPortalWrapper(wrapper, pointAccess.varName(), argsValues, parentContext, interpreter, onGoingRuntime);
			
			accessValue = hostValue.pointGet(pointAccess.varName());
			if(accessValue == null)
				return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
		} else {
			accessValue = onGoingRuntime.register(access.interpret(parentContext, interpreter, onGoingRuntime), access.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		
		if(accessValue instanceof ICallable callable) {
			int argNumber = callable.numberOfArguments();
			if(argNumber != -1 && argNumber != argsValues.length)
				return onGoingRuntime.failure(new IncorrectNumberOfArguments(argNumber, argsValues.length, accessValue.typeName(), sequence));
			
			Value returnedValue = onGoingRuntime.register(callable.call(hostValue, parentContext, interpreter, onGoingRuntime, argsValues), sequence);
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			return onGoingRuntime.buffer(returnedValue);
		} else return onGoingRuntime.failure(new CannotCallError(accessValue.typeName(), access.sequence()));
	}
	
	private RuntimeResult handleFoxPortalWrapper(FoxPortalWrapper wrapper, String methodName, Value[] args, Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value value = onGoingRuntime.register(wrapper.callMethod(onGoingRuntime, methodName, args), sequence);
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		return onGoingRuntime.buffer(value);
	}
	
}