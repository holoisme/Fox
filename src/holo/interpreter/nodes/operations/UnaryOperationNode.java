package holo.interpreter.nodes.operations;

import holo.errors.IllegalOperationError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record UnaryOperationNode(UnaryOperationType operation, Node node, Sequence sequence) implements Node {
	
	public String toString() {
		return operation + "" + node;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Value expression = onGoingRuntime.register(node.interpret(parentContext, interpreter, onGoingRuntime), node.sequence());
		if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		
		Value result = expression.unaryOperation(operation);
		if(result == null)
			return onGoingRuntime.failure(new IllegalOperationError(operation.toString(), expression.typeName(), sequence));
		
		return onGoingRuntime.buffer(result);
	}
	
}