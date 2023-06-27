package holo.interpreter.nodes.operations;

import holo.errors.IllegalOperationError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record UnaryOperationNode(UnaryOperationType operation, Node node, Sequence sequence) implements Node {
	
	public String toString() {
		return operation + "" + node;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value expression = node.interpret(parentContext, interpreter);
		
		Value result = expression.unaryOperation(operation);
		
		if(result == null)
			throw new TError(new IllegalOperationError(operation.toString(), expression.typeName(), sequence));
		
		return result;
	}
	
}