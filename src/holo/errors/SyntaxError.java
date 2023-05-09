package holo.errors;

import holo.lang.lexer.Token;

public class SyntaxError extends FoxError {
	
	private Token token;
	private String expected;
	
	public SyntaxError(Token token) {
		super("Syntax Error", token.sequence());
		this.token = token;
		this.expected = null;
	}
	
	public SyntaxError(Token token, String expected) {
		super("Syntax Error", token.sequence());
		this.token = token;
		this.expected = expected;
	}
	
	public String toString() {
		return expected==null ?
				"Unexpected " + token.toStringFull():
				"Expected " + expected + " but got " + token.toStringFull();
	}

}
