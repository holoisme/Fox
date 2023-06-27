package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.ErrorValue;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record TryCatchNode(Node tryBody, Node catchBody, String errorVarName, Sequence sequence) implements Node {

	public String toString() {
		return "try " + tryBody + " catch("+errorVarName+") " + catchBody;
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		try {
			Value value = tryBody.interpret(parentContext, interpreter);
			
			return value == Value.NULL ? BooleanValue.TRUE : value;
		} catch(TError t) {
			Context catchContext = new SimpleContext("catch", parentContext);
			catchContext.setToThis(errorVarName, new ErrorValue(t.error()));
			Value value = catchBody.interpret(catchContext, interpreter);
			
			return value == Value.NULL ? BooleanValue.FALSE : value;
		}
	}


}
