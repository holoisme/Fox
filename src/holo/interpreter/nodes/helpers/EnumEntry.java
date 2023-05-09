package holo.interpreter.nodes.helpers;

import java.util.Arrays;

import holo.interpreter.nodes.Node;
import holo.lang.lexer.Sequence;

public record EnumEntry(String name, Node[] args, Sequence sequence) {
	
	public String toString() {
		return name + "("+Arrays.toString(args)+")";
	}
	
}
