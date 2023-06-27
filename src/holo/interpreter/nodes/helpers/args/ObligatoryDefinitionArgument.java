package holo.interpreter.nodes.helpers.args;

import holo.interpreter.nodes.Sequenced;
import holo.lang.lexer.Sequence;

public record ObligatoryDefinitionArgument(String name, Sequence sequence) implements SingleDefinitionArgument, Sequenced {
	
	public String toString() {
		return name;
	}
	
	public boolean isObligatory() {
		return true;
	}
	
}
