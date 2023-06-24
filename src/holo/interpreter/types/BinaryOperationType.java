package holo.interpreter.types;

import holo.lang.lexer.Token;
import holo.lang.lexer.TokenType;

public enum BinaryOperationType {
	
	PLUS("+"), MINUS("-"), MULTIPLY("*"), DIVIDE("/"), EXPONANT("^"), MODULO("%"),
//	AND("and"), OR("or"),
	DOUBLE_EQUALS("=="), NOT_EQUAL("!="), LESS_THAN("<"), GREATER_THAN(">"), LESS_OR_EQUAL("<="), GREATER_OR_EQUAL(">=");
	
	private String symbol;
	
	private BinaryOperationType(String symbol) {
		this.symbol = symbol;
	}
	
	public String toString() { return symbol; }
	
	public static BinaryOperationType get(Token token) {
		TokenType type = token.type();
		
		if(type == TokenType.PLUS || type == TokenType.PLUS_EQUAL) return PLUS;
		else if(type == TokenType.MINUS || type == TokenType.MINUS_EQUAL) return MINUS;
		else if(type == TokenType.MULTIPLY || type == TokenType.MULTIPLY_EQUAL) return MULTIPLY;
		else if(type == TokenType.DIVIDE || type == TokenType.DIVIDE_EQUAL) return DIVIDE;
		else if(type == TokenType.EXPONANT || type == TokenType.EXPONANT_EQUAL) return EXPONANT;
		else if(type == TokenType.MODULO || type == TokenType.MODULO_EQUAL) return MODULO;
		
		else if(type == TokenType.DOUBLE_EQUALS) return DOUBLE_EQUALS;
		else if(type == TokenType.NOT_EQUAL) return NOT_EQUAL;
		else if(type == TokenType.LESS_THAN) return LESS_THAN;
		else if(type == TokenType.GREATER_THAN) return GREATER_THAN;
		else if(type == TokenType.LESS_OR_EQUAL) return LESS_OR_EQUAL;
		else if(type == TokenType.GREATER_OR_EQUAL) return GREATER_OR_EQUAL;
		
//		else if(token.content().equals("and")) return AND;
//		else if(token.content().equals("or")) return OR;
		
		return null;
	}
	
}
