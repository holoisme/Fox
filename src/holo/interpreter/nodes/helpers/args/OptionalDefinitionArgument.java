package holo.interpreter.nodes.helpers.args;

import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.Sequenced;
import holo.lang.lexer.Sequence;

public record OptionalDefinitionArgument(String name, Node defaultExpression, Sequence sequence) implements SingleDefinitionArgument, Sequenced {
	
	public String toString() {
		return name + " = " + defaultExpression;
	}
	
	public boolean isObligatory() {
		return false;
	}
	
}
