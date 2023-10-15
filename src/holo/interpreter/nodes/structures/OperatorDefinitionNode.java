package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.Sequenced;
import holo.interpreter.transcendental.TReturn;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record OperatorDefinitionNode(BinaryOperationType operation, String argumentName, Node body, Sequence sequence) implements Sequenced {

	@Override
	public String toString() {
		return "operator " + operation.getSymbol() + "(" + argumentName + ")" + body.toString();
	}
	
	public Value binaryOperation(Value argument, Context insideContext, Interpreter interpreter, Sequence sequence) {
		final Context callContext = new SimpleContext("operator", insideContext, true);
		callContext.setToThis(argumentName, argument);
		
		try {
			body.interpret(callContext, interpreter);
		} catch(TReturn t) {
			return t.value();
		}
		
		return Value.UNDEFINED;
	}
	
}
