package holo.interpreter.nodes.calls;

import holo.errors.CannotAccessError;
import holo.errors.CannotCallError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.var.VarPointAccessNode;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IInstanciable;
import holo.lang.lexer.Sequence;

public record NewInstanceCallNode(CallNode call, Sequence sequence) implements Node {

	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		
		Value hostValue = null;
		Value accessValue = null;
		
		if(call.access() instanceof VarPointAccessNode pointAccess) {
			hostValue = onGoingRuntime.register(pointAccess.access().interpret(parentContext, interpreter, onGoingRuntime), pointAccess.access().sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			accessValue = hostValue.pointGet(pointAccess.varName());
			if(accessValue == null)
				return onGoingRuntime.failure(new CannotAccessError(hostValue.typeName(), call.access().sequence()));
		} else {
			accessValue = onGoingRuntime.register(call.access().interpret(parentContext, interpreter, onGoingRuntime), call.access().sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		
		Value[] argsValues = new Value[call.args().length];
		for(int i = 0; i < call.args().length; i++) {
			argsValues[i] = onGoingRuntime.register(call.args()[i].interpret(parentContext, interpreter, onGoingRuntime), call.args()[i].sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		
		if(accessValue instanceof IInstanciable instanciable) {
			Value value = onGoingRuntime.register(instanciable.createInstance(interpreter, onGoingRuntime, argsValues), null);
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			return onGoingRuntime.buffer(value);
		} else return onGoingRuntime.failure(new CannotCallError(accessValue.typeName(), call.access().sequence()));
	}
	
	public String toString() {
		return "(new " + call.toString()+")";
	}

}
