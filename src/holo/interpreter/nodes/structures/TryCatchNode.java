package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record TryCatchNode(Node tryBody, Node catchBody, String errorVarName, Sequence sequence) implements Node {

	public String toString() {
		return "try " + tryBody + " catch("+errorVarName+") " + catchBody;
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		try {
			final Value value = tryBody.interpret(parentContext, interpreter);
			
			return value == Value.UNDEFINED ? BooleanValue.TRUE : value;
		} catch(TError t) {
			final Context catchContext = new SimpleContext("catch", parentContext, false);
			catchContext.setToThis(errorVarName, t.error());
			final Value value = catchBody.interpret(catchContext, interpreter);
			
			return value == Value.UNDEFINED ? BooleanValue.FALSE : value;
		}
	}

}
