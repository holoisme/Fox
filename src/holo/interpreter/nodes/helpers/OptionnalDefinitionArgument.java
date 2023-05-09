package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.Node;

public record OptionnalDefinitionArgument(String name, Node defaultExpression) implements SingleDefinitionArgument {
	
	public String toString() {
		return name + " = " + defaultExpression;
	}
	
	public boolean isObligatory() {
		return false;
	}
	
}
