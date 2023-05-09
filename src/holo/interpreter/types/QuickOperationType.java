package holo.interpreter.types;

import holo.lang.lexer.Token;
import holo.lang.lexer.TokenType;

public enum QuickOperationType {
	
	PLUS_PLUS("++"), MINUS_MINUS("--");
	
	private String symbol;
	
	private QuickOperationType(String symbol) {
		this.symbol = symbol;
	}
	
	public String toString() { return symbol; }
	
	public static QuickOperationType get(Token token) {
		if(token.type() == TokenType.PLUS_PLUS) return PLUS_PLUS;
		else if(token.type() == TokenType.MINUS_MINUS) return MINUS_MINUS;
		
		return null;
	}
	
}
