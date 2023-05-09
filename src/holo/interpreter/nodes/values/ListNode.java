package holo.interpreter.nodes.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.ListValue;
import holo.lang.lexer.Sequence;

public record ListNode(Node[] nodes, Sequence sequence) implements Node {
	
	public String toString() {
		return Arrays.toString(nodes);
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		List<Value> elements = new ArrayList<>();
		
		for(Node elementNode: nodes) {
			Value element = onGoingRuntime.register(elementNode.interpret(parentContext, interpreter, onGoingRuntime), elementNode.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			
			elements.add(element);
		}
		
		return onGoingRuntime.buffer(new ListValue(elements));
	}
	
}