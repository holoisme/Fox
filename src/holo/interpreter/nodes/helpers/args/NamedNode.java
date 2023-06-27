package holo.interpreter.nodes.helpers.args;

import holo.interpreter.nodes.Node;

public record NamedNode(String name, Node node) {
	
	public String toString() {
		return name + " = " + node;
	}
	
}
