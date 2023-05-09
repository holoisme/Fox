package holo.interpreter.nodes.structures;

import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.ErrorValue;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record TryCatchNode(Node tryBody, Node catchBody, String errorVarName, Sequence sequence) implements Node {

	public String toString() {
		return "try " + tryBody + " catch("+errorVarName+") " + catchBody;
	}
	
	@Override
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		rt.register(tryBody.interpret(parentContext, interpreter, rt), tryBody.sequence());
		
		if(rt.hasError()) {
			RuntimeError error = rt.getError();
			rt.clearError();
			
			Context catchContext = new SimpleContext("catch", parentContext);
			catchContext.setToThis(errorVarName, new ErrorValue(error));
			
			rt.register(catchBody.interpret(catchContext, interpreter, rt), catchBody.sequence());
			if(rt.shouldReturn()) return rt;
			
			return rt.success(BooleanValue.FALSE);
		} else if(rt.shouldReturn()) return rt;
		
		return rt.success(BooleanValue.TRUE);
	}


}
