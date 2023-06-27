package holo.interpreter.nodes.operations;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.types.BinaryBooleanOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.lang.lexer.Sequence;

public record BinaryBooleanOperationNode(BinaryBooleanOperationType operation, Node left, Node right, Sequence sequence) implements Node {
	
	public String toString() {
		return left + " " + operation.toString() + " " + right;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Value leftValue = left.interpret(parentContext, interpreter);
		
		if(operation == BinaryBooleanOperationType.AND) {
			if(!leftValue.isTrue())
				return BooleanValue.FALSE;
			
			return BooleanValue.get(right.interpret(parentContext, interpreter).isTrue());
		} else if(operation == BinaryBooleanOperationType.OR) {
			if(leftValue.isTrue())
				return BooleanValue.TRUE;
			
			return BooleanValue.get(right.interpret(parentContext, interpreter).isTrue());
		}
		
		return Value.NULL;
	}
	
}