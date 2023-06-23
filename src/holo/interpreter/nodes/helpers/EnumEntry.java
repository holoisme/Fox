package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.lang.lexer.Sequence;

public record EnumEntry(String name, Node[] args, Sequence sequence) {
	
	public String toString() {
		return name + "("+ReflectionUtils.toString(args)+")";
	}
	
}
