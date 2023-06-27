package holo.interpreter.nodes.helpers.args;

import holo.interpreter.values.Value;

public record NamedValue(String name, Value value) {
	
	public String toString() {
		return name + " = " + value;
	}
	
}
