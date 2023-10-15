package holo.interpreter.nodes.calls;

import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.lang.lexer.Sequence;

public record SuperConstructorCall(Node[] args, NamedNode[] optionalArguments, Sequence sequence) {
	
	public String toString() {
		return "super("+ReflectionUtils.toString(args)+")";
	}

}
