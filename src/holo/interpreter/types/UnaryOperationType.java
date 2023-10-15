package holo.interpreter.types;

public enum UnaryOperationType {
	
	NEGATE("-"), LOGICAL_NOT("~");
	
	private String symbol;
	
	private UnaryOperationType(String symbol) {
		this.symbol = symbol;
	}
	
	public String toString() { return symbol; }
}
