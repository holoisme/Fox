package holo.interpreter.types;

import holo.lang.lexer.Token;

public enum BinaryBooleanOperationType {
	
	AND("and"), OR("or");
	
	private String symbol;
	
	private BinaryBooleanOperationType(String symbol) {
		this.symbol = symbol;
	}
	
	public String toString() { return symbol; }
	
	public static BinaryBooleanOperationType get(Token token) {
		if(token.content().equals("and")) return AND;
		else if(token.content().equals("or")) return OR;
		
		return null;
	}
	
}
