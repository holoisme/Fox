package holo.interpreter.nodes.values;

import java.util.HashMap;
import java.util.Map;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.ObjectStatementSequence;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.ObjectValue;
import holo.lang.lexer.Sequence;

public record ObjectNode(ObjectStatementSequence[] statements, Sequence sequence) implements Node {
	
	public String toString() {
		return "{ "+ReflectionUtils.toString(statements) + " }";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		Map<String, Value> map = new HashMap<>();
		
		ObjectValue objectValue = new ObjectValue(map, parentContext);
		
		for(ObjectStatementSequence objectStatement: statements)
			map.put(objectStatement.name(), objectStatement.expression().interpret(objectValue, interpreter));
		
		return objectValue;
	}
	
}