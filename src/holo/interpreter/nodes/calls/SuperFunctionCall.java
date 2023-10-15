package holo.interpreter.nodes.calls;

import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record SuperFunctionCall(String functionName, Node[] args, NamedNode[] optionalArguments, Sequence sequence) implements Node {

	@Override
	public String toString() {
		return "super."+ functionName + "("+ReflectionUtils.toString(args)+")";
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Context firstSuperableParentContext = parentContext.getFirstSuperableParent();
		
		if(firstSuperableParentContext == null)
			throw new TError(new RuntimeError("No super-able context", sequence));
		
		final Value[] argsValues = new Value[args.length];
		for(int i = 0; i < args.length; i++)
			argsValues[i] = args[i].interpret(parentContext, interpreter);
		
		final NamedValue[] optionalValues = new NamedValue[optionalArguments.length];
		for(int i = 0; i < optionalArguments.length; i++)
			optionalValues[i] = new NamedValue(optionalArguments[i].name(), optionalArguments[i].node().interpret(parentContext, interpreter));
		
		return firstSuperableParentContext.superValue().executeFunction(functionName, firstSuperableParentContext.thisValue(), firstSuperableParentContext, argsValues, optionalValues, interpreter, sequence);
	}

}
