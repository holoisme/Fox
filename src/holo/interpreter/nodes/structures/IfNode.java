package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.ConditionnedSequence;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record IfNode(ConditionnedSequence[] conditions, Node elseBodyNode, Sequence sequence) implements Node {
	
	public String toString() {
		String str = "if "+ conditions[0];
		for(int i = 1; i < conditions.length; i++)
			str += "\nelse if " + conditions[i];
		if(elseBodyNode != null)
			str += "\nelse " + elseBodyNode;
		return str;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		for(ConditionnedSequence conditionnedSequence:conditions) {
			Value conditionValue = rt.register(conditionnedSequence.condition().interpret(parentContext, interpreter, rt), conditionnedSequence.condition().sequence());
			if(rt.shouldReturn()) return rt;
			
			if(conditionValue.isTrue()) {
				Value valueToReturn = rt.register(conditionnedSequence.body().interpret(parentContext, interpreter, rt), conditionnedSequence.body().sequence());
				if(rt.shouldReturn()) return rt;
				
				return rt.success(valueToReturn);
			}
		}
		
		if(elseBodyNode == null)
			return rt.success(Value.NULL);
		
		Value elseValueToReturn = rt.register(elseBodyNode.interpret(parentContext, interpreter, rt), elseBodyNode.sequence());
		if(rt.shouldReturn()) return rt;
		
		return rt.success(elseValueToReturn);
	}
	
}