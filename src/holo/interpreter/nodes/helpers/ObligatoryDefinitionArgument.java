package holo.interpreter.nodes.helpers;

public record ObligatoryDefinitionArgument(String name) implements SingleDefinitionArgument {
	
	public String toString() {
		return name;
	}
	
	public boolean isObligatory() {
		return true;
	}
	
}
