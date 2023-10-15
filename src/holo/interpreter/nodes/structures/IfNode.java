package holo.interpreter.nodes.structures;

import holo.interpreter.Interpreter;
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
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		for(ConditionnedSequence conditionnedSequence:conditions) {
			final Value conditionValue = conditionnedSequence.condition().interpret(parentContext, interpreter);
			
			if(conditionValue.isTrue())
				return conditionnedSequence.body().interpret(parentContext, interpreter);
		}
		
		return elseBodyNode == null ? Value.UNDEFINED : elseBodyNode.interpret(parentContext, interpreter);
	}
	
}