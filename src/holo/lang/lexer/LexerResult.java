package holo.lang.lexer;

import java.util.List;

import holo.errors.LexerError;

public record LexerResult(List<Token> tokens, String originalText, String fileName, LexerError error) {
	
	public boolean hasError() { return error != null; }
	
}
