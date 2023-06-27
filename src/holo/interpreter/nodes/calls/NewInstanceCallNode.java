package holo.interpreter.nodes.calls;

import holo.errors.CannotAccessError;
import holo.errors.CannotCallError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.nodes.var.VarPointAccessNode;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IInstanciable;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record NewInstanceCallNode(CallNode call, Sequence sequence) implements Node {

	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value hostValue = null;
		Value accessValue = null;
		
		if(call.access() instanceof VarPointAccessNode pointAccess) {
			hostValue = pointAccess.access().interpret(parentContext, interpreter);
			
			accessValue = hostValue.pointGet(pointAccess.varName());
			if(accessValue == null)
				throw new TError(new CannotAccessError(hostValue.typeName(), call.access().sequence()));
		} else accessValue = call.access().interpret(parentContext, interpreter);
		
		Value[] argsValues = new Value[call.args().length];
		for(int i = 0; i < call.args().length; i++)
			argsValues[i] = call.args()[i].interpret(parentContext, interpreter);
		
		final NamedNode[] optionalArguments = call.optionalArguments();
		
		NamedValue[] optionalValues = new NamedValue[optionalArguments.length];
		for(int i = 0; i < optionalArguments.length; i++)
			optionalValues[i] = new NamedValue(optionalArguments[i].name(), optionalArguments[i].node().interpret(parentContext, interpreter));
		
		if(accessValue instanceof IInstanciable instanciable) {
			return instanciable.createInstance(interpreter, sequence, argsValues, optionalValues);
		} else throw new TError(new CannotCallError(accessValue.typeName(), call.access().sequence()));
	}
	
	public String toString() {
		return "(new " + call.toString()+")";
	}

}
