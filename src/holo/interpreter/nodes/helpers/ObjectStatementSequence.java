package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.Sequenced;
import holo.lang.lexer.Sequence;

public record ObjectStatementSequence(String name, Node expression, Sequence sequence) implements Sequenced {
	
	public String toString() {
		return name.toString() + ": " + expression;
	}
	
}