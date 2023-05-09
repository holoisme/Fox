package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.Sequenced;
import holo.lang.lexer.Sequence;

public record ConditionnedSequence(Node condition, Node body, Sequence sequence) implements Sequenced {
	
	public String toString() {
		return condition + ": " + body;
	}
	
}