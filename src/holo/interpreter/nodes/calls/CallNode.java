package holo.interpreter.nodes.calls;

import holo.errors.CannotCallError;
import holo.errors.IncorrectNumberOfArguments;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.nodes.var.OptionalChainingNode;
import holo.interpreter.nodes.var.VarAccessNode;
import holo.interpreter.nodes.var.VarPointAccessNode;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallHandler;
import holo.interpreter.values.interfaces.ICallable;
import holo.lang.lexer.Sequence;

public record CallNode(Node access, Node[] args, NamedNode[] optionalArguments, Sequence sequence) implements Node {
	
	public String toString() {
		return access.toString() + "("+ReflectionUtils.toString(args)+")";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value hostValue = null;
		Value accessValue = null;
		
		final Value[] argsValues = new Value[args.length];
		for(int i = 0; i < args.length; i++)
			argsValues[i] = args[i].interpret(parentContext, interpreter);
		
		final NamedValue[] optionalValues = new NamedValue[optionalArguments.length];
		for(int i = 0; i < optionalArguments.length; i++)
			optionalValues[i] = new NamedValue(optionalArguments[i].name(), optionalArguments[i].node().interpret(parentContext, interpreter));
		
		if(access instanceof VarAccessNode var) {
			final String varName = var.varName();
			Context container = parentContext.getFirstParentThatHasKey(varName);
			
			if(container instanceof ICallHandler wrapper)
				return wrapper.callMethod(varName, argsValues, optionalValues, interpreter, sequence);
			accessValue = container.get(varName);
		} else if(access instanceof VarPointAccessNode pointAccess) {
			hostValue = pointAccess.access().interpret(parentContext, interpreter);
			
			if(hostValue instanceof ICallHandler wrapper)
				return wrapper.callMethod(pointAccess.varName(), argsValues, optionalValues, interpreter, sequence);
			
			accessValue = hostValue.pointGet(pointAccess.varName(), sequence);
		} else if(access instanceof OptionalChainingNode pointAccess) {
			hostValue = pointAccess.access().interpret(parentContext, interpreter);
			
			if(hostValue.nullOrUndefined())
				return Value.UNDEFINED;
			
			if(hostValue instanceof ICallHandler wrapper)
				return wrapper.callMethod(pointAccess.varName(), argsValues, optionalValues, interpreter, sequence);
			
			accessValue = hostValue.pointGet(pointAccess.varName(), sequence);
		} else accessValue = access.interpret(parentContext, interpreter);
		
		if(accessValue instanceof ICallable callable) {
			final int argNumber = callable.numberOfArguments();
			if(argNumber != -1 && argNumber != argsValues.length)
				throw new TError(new IncorrectNumberOfArguments(argNumber, argsValues.length, accessValue.typeName(), sequence));
			
			try {
				return callable.call(hostValue, parentContext, interpreter, sequence, argsValues, optionalValues);
			} catch(TError error) {
				throw error.addToTrace(access.toString(), sequence);
			}
		} else throw new TError(new CannotCallError(accessValue.typeName(), access.sequence()));
	}
	
//	private Value handleFoxPortalWrapper(ICallHandler wrapper, String methodName, Value[] args, Interpreter interpreter, Sequence sequence) {
//		return wrapper.callMethod(methodName, args, interpreter, sequence);
//	}
	
}