package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.Sequenced;
import holo.lang.lexer.Sequence;

public record DefinitionArgument(SingleDefinitionArgument argument, Sequence sequence) implements Sequenced {
	
	public String toString() {
		return argument.toString();
	}
	
}