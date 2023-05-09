package holo.interpreter.types;

public enum UnaryOperationType {
	
	NOT("not"), NEGATE("-");
	
	private String symbol;
	
	private UnaryOperationType(String symbol) {
		this.symbol = symbol;
	}
	
	public String toString() { return symbol; }
}
