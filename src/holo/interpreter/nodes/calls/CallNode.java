package holo.interpreter.nodes.calls;

import holo.errors.CannotAccessError;
import holo.errors.CannotCallError;
import holo.errors.IncorrectNumberOfArguments;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.nodes.var.OptionalChainingNode;
import holo.interpreter.nodes.var.VarPointAccessNode;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallHandler;
import holo.interpreter.values.interfaces.ICallable;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record CallNode(Node access, Node[] args, NamedNode[] optionalArguments, Sequence sequence) implements Node {
	
	public String toString() {
		return access.toString() + "("+ReflectionUtils.toString(args)+")";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value hostValue = null;
		Value accessValue = null;
		
		Value[] argsValues = new Value[args.length];
		for(int i = 0; i < args.length; i++)
			argsValues[i] = args[i].interpret(parentContext, interpreter);
		
		NamedValue[] optionalValues = new NamedValue[optionalArguments.length];
		for(int i = 0; i < optionalArguments.length; i++)
			optionalValues[i] = new NamedValue(optionalArguments[i].name(), optionalArguments[i].node().interpret(parentContext, interpreter));
		
		if(access instanceof VarPointAccessNode pointAccess) {
			hostValue = pointAccess.access().interpret(parentContext, interpreter);
			
			if(hostValue instanceof ICallHandler wrapper)
				return handleFoxPortalWrapper(wrapper, pointAccess.varName(), argsValues, parentContext, interpreter);
			
			accessValue = hostValue.pointGet(pointAccess.varName());
			if(accessValue == null)
				throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
		} else if(access instanceof OptionalChainingNode pointAccess) {
			hostValue = pointAccess.access().interpret(parentContext, interpreter);
			
			if(hostValue == Value.NULL)
				return Value.NULL;
			
			if(hostValue instanceof ICallHandler wrapper)
				return handleFoxPortalWrapper(wrapper, pointAccess.varName(), argsValues, parentContext, interpreter);
			
			accessValue = hostValue.pointGet(pointAccess.varName());
			if(accessValue == null)
				throw new TError(new CannotAccessError(hostValue.typeName(), access.sequence()));
		} else accessValue = access.interpret(parentContext, interpreter);
		
		if(accessValue instanceof ICallable callable) {
			int argNumber = callable.numberOfArguments();
			if(argNumber != -1 && argNumber != argsValues.length)
				throw new TError(new IncorrectNumberOfArguments(argNumber, argsValues.length, accessValue.typeName(), sequence));
			
			return callable.call(hostValue, parentContext, interpreter, argsValues, optionalValues);
		} else throw new TError(new CannotCallError(accessValue.typeName(), access.sequence()));
	}
	
	private Value handleFoxPortalWrapper(ICallHandler wrapper, String methodName, Value[] args, Context parentContext, Interpreter interpreter) {
		return wrapper.callMethod(methodName, args);
	}
	
}