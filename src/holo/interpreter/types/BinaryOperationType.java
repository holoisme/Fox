package holo.interpreter.types;

import holo.lang.lexer.Token;

public enum BinaryOperationType {
	
	PLUS("+", "add"), MINUS("-", "sub"), MULTIPLY("*", "mult"), DIVIDE("/", "div"), EXPONANT("**", "exp"), MODULO("%", "mod"),
	DOUBLE_EQUALS("==", "equal"), NOT_EQUAL("!=", "nequal"), LESS_THAN("<", "less"), GREATER_THAN(">", "greater"), LESS_OR_EQUAL("<=", "lesseq"), GREATER_OR_EQUAL(">=", "greatereq"),
	LOGICAL_SHIFT_LEFT("<<", "lsleft"), LOGICAL_SHIFT_RIGHT(">>", "lsright"), LOGICAL_UNSIGNED_SHIFT_RIGHT(">>>", "lusright"), LOGICAL_XOR("^", "lxor"), LOGICAL_AND("&", "land"), LOGICAL_OR("|", "lor");
	
	private String symbol, name;
	
	private BinaryOperationType(String symbol, String name) {
		this.symbol = symbol;
		this.name = name;
	}
	
	public String toString() { return symbol; }
	
	public String getName() { return name; }
	public String getSymbol() { return symbol; }
	
	public static BinaryOperationType get(Token token) {
		
		switch(token.type()) {
			case PLUS, PLUS_EQUAL: return PLUS;
			case MINUS, MINUS_EQUAL: return MINUS;
			case MULTIPLY, MULTIPLY_EQUAL: return MULTIPLY;
			case DIVIDE, DIVIDE_EQUAL: return DIVIDE;
			case EXPONANT, EXPONANT_EQUAL: return EXPONANT;
			case MODULO, MODULO_EQUAL: return MODULO;
			
			case DOUBLE_EQUALS: return DOUBLE_EQUALS;
			case NOT_EQUAL: return NOT_EQUAL;
			case LESS_THAN: return LESS_THAN;
			case GREATER_THAN: return GREATER_THAN;
			case LESS_OR_EQUAL: return LESS_OR_EQUAL;
			case GREATER_OR_EQUAL: return GREATER_OR_EQUAL;
			
			case LOGICAL_SHIFT_LEFT, LOGICAL_SHIFT_LEFT_EQUAL: return LOGICAL_SHIFT_LEFT;
			case LOGICAL_SHIFT_RIGHT, LOGICAL_SHIFT_RIGHT_EQUAL: return LOGICAL_SHIFT_RIGHT;
			case LOGICAL_UNSIGNED_SHIFT_RIGHT, LOGICAL_STRICT_SHIFT_RIGHT_EQUAL: return LOGICAL_UNSIGNED_SHIFT_RIGHT;
			case LOGICAL_XOR, LOGICAL_XOR_EQUAL: return LOGICAL_XOR;
			case LOGICAL_AND, LOGICAL_AND_EQUAL: return LOGICAL_AND;
			case LOGICAL_OR, LOGICAL_OR_EQUAL: return LOGICAL_OR;
			
			default: throw new RuntimeException("Unexpected token passed " + token.toStringFull());
		}
		
//		if(type == TokenType.PLUS || type == TokenType.PLUS_EQUAL) return PLUS;
//		else if(type == TokenType.MINUS || type == TokenType.MINUS_EQUAL) return MINUS;
//		else if(type == TokenType.MULTIPLY || type == TokenType.MULTIPLY_EQUAL) return MULTIPLY;
//		else if(type == TokenType.DIVIDE || type == TokenType.DIVIDE_EQUAL) return DIVIDE;
//		else if(type == TokenType.EXPONANT || type == TokenType.EXPONANT_EQUAL) return EXPONANT;
//		else if(type == TokenType.MODULO || type == TokenType.MODULO_EQUAL) return MODULO;
//		
//		else if(type == TokenType.DOUBLE_EQUALS) return DOUBLE_EQUALS;
//		else if(type == TokenType.NOT_EQUAL) return NOT_EQUAL;
//		else if(type == TokenType.LESS_THAN) return LESS_THAN;
//		else if(type == TokenType.GREATER_THAN) return GREATER_THAN;
//		else if(type == TokenType.LESS_OR_EQUAL) return LESS_OR_EQUAL;
//		else if(type == TokenType.GREATER_OR_EQUAL) return GREATER_OR_EQUAL;
	}
	
}
