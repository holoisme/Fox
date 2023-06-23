package holo.errors;

import holo.lang.lexer.Sequence;

public class LexerError extends FoxError {

	public LexerError(char c, Sequence sequence) {
		super("The character '" + c + "' is not recognized", sequence);
	}
	
	public String toString() {
		return "Lexer Error";
	}
	
}
