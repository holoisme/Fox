package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.lang.lexer.Sequence;

public record EnumEntry(String name, Node[] args, NamedNode[] optionalArgs, Sequence sequence) {
	
	public String toString() {
		return name + "("+ReflectionUtils.toString(args)+")";
	}
	
}
