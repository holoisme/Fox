package holo.interpreter.nodes.values;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.ObjectStatementSequence;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.ObjectValue;
import holo.lang.lexer.Sequence;

public record ObjectNode(ObjectStatementSequence[] statements, Sequence sequence) implements Node {
	
	public String toString() {
		return Arrays.toString(statements);
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		Map<String, Value> map = new HashMap<>();
		
		ObjectValue objectValue = new ObjectValue(map, parentContext);
		
		for(ObjectStatementSequence objectStatement: statements) {
			Value element = onGoingRuntime.register(objectStatement.expression().interpret(objectValue, interpreter, onGoingRuntime), objectStatement.expression().sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			map.put(objectStatement.name(), element);
		}
		
		return onGoingRuntime.buffer(objectValue);
	}
	
}