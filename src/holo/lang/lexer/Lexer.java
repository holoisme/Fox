package holo.lang.lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import holo.errors.LexerError;

public class Lexer {
	
	private Lexer() {}
	
	public static final String DIGITS = "0123456789";
	public static final String STARTING_IDENTIFIER_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ$_";
	public static final String IDENTIFIER_CHARS = STARTING_IDENTIFIER_CHARS + DIGITS;
	public static final String HEX_CHARS = DIGITS + "abcdefABCDEF";
	
	private static final Map<Character, String> STRING_ESCAPE_CHARS = Map.ofEntries(Map.entry('n', "\n"), Map.entry('t', "\t"), Map.entry('\\', "\\"));
	
	public static final String[] KEYWORDS = {
		"var", "function", "if", "else", "for", "in", "while", "do", "try", "catch", "switch", "case",
		"extract", "from", "library", "import", "as",
		"return", "break", "continue", "assert",
		"this",
		"and", "or", "not",
		"true", "false", "null",
		"int", "float", "double", "char", "string", "boolean",
		"class", "enum", "constructor", "static", "new",
		"extends", "super"
	};
	
	public static LexerResult extract(File file) throws IOException {
		return extract(new FileInputStream(file), file.getName());
	}
	
	public static LexerResult extract(InputStream is, String fileName) throws IOException {
		String text = new String(is.readAllBytes());
		return extract(text, fileName);
	}
	
	public static LexerResult extract(String text) { return extract(text, "<anonymous file>"); }
	
	public static LexerResult extract(String text, String fileName) {
		List<Token> tokens = new ArrayList<>();
		
		int line = 1;
		int row = 0;
		char[] chars = text.toCharArray();
		int lastIndex = -1;
		
		LexerError error = null;
		
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];
			
			row += i - lastIndex;
			lastIndex = i;
			
			if(c == ' ' || c == '\t') continue;
			
			if(c == '\n') {
				line++;
				row = 0;
				continue;
			}
			
			if(c == ';') tokens.add(new Token(";", TokenType.DELIMITER, Sequence.from(line, row, 1, fileName)));
			else if(c == '(') tokens.add(new Token("(", TokenType.LEFT_PARENTHESE, Sequence.from(line, row, 1, fileName)));
			else if(c == ')') tokens.add(new Token(")", TokenType.RIGHT_PARENTHESE, Sequence.from(line, row, 1, fileName)));
			else if(c == '[') tokens.add(new Token("[", TokenType.LEFT_BRACKET, Sequence.from(line, row, 1, fileName)));
			else if(c == ']') tokens.add(new Token("]", TokenType.RIGHT_BRACKET, Sequence.from(line, row, 1, fileName)));
			else if(c == '{') tokens.add(new Token("{", TokenType.LEFT_CURLY_BRACKET, Sequence.from(line, row, 1, fileName)));
			else if(c == '}') tokens.add(new Token("}", TokenType.RIGHT_CURLY_BRACKET, Sequence.from(line, row, 1, fileName)));
			else if(c == ':') tokens.add(new Token(":", TokenType.COLON, Sequence.from(line, row, 1, fileName)));
			else if(c == ',') tokens.add(new Token(",", TokenType.COMMA, Sequence.from(line, row, 1, fileName)));
//			else if(c == '?') tokens.add(new Token("?", TokenType.QUESTION_MARK, Sequence.from(line, row, 1, fileName)));
//			else if(c == '?') i = buildWhenNextCharacterIs(i, c, '?', TokenType.QUESTION_MARK, TokenType.DOUBLE_QUESTION_MARK, chars, line, row, fileName, tokens);
			else if(c == '?') i = buildQuestionMark(i, chars, line, row, fileName, tokens);
			
			else if(c == '.') i = buildPoint(i, chars, line, row, fileName, tokens);
			else if(c == '!') i = buildExclamationMark(i, chars, line, row, fileName, tokens);
			else if(c == '&') i = buildBooleanOp(i, c, chars, "and", line, row, fileName, tokens);
			else if(c == '|') i = buildBooleanOp(i, c, chars, "or", line, row, fileName, tokens);
			
//			else if(c == '=') i = buildWhenNextCharacterIs(i, c, '=', TokenType.EQUALS, TokenType.DOUBLE_EQUALS, chars, line, row, fileName, tokens);
			else if(c == '=') i = buildEqual(i, chars, line, row, fileName, tokens);
			
			else if(c == '<') i = buildWhenNextCharacterIs(i, c, '=', TokenType.LESS_THAN, TokenType.LESS_OR_EQUAL, chars, line, row, fileName, tokens);
			else if(c == '>') i = buildWhenNextCharacterIs(i, c, '=', TokenType.GREATER_THAN, TokenType.GREATER_OR_EQUAL, chars, line, row, fileName, tokens);
			
			else if(c == '*') i = buildWhenNextCharacterIs(i, c, '=', TokenType.MULTIPLY, TokenType.MULTIPLY_EQUAL, chars, line, row, fileName, tokens);
			else if(c == '/') i = buildWhenNextCharacterIs(i, c, '=', TokenType.DIVIDE, TokenType.DIVIDE_EQUAL, chars, line, row, fileName, tokens);
			else if(c == '^') i = buildWhenNextCharacterIs(i, c, '=', TokenType.EXPONANT, TokenType.EXPONANT_EQUAL, chars, line, row, fileName, tokens);
			else if(c == '%') i = buildWhenNextCharacterIs(i, c, '=', TokenType.MODULO, TokenType.MODULO_EQUAL, chars, line, row, fileName, tokens);
			
			else if(c == '+') i = buildBinaryOP(i, c, TokenType.PLUS, TokenType.PLUS_EQUAL, TokenType.PLUS_PLUS, chars, line, row, fileName, tokens);
			else if(c == '-') i = buildBinaryOP(i, c, TokenType.MINUS, TokenType.MINUS_EQUAL, TokenType.MINUS_MINUS, chars, line, row, fileName, tokens);
			
			else if(DIGITS.contains(c+"")) i = buildNumber(i, chars, line, row, fileName, tokens);
			else if(STARTING_IDENTIFIER_CHARS.contains(c+"")) i = buildIdentifier(i, chars, line, row, fileName, tokens);
			
			else if(c == '"') i = buildString(i, '"', chars, line, row, fileName, tokens);
			else if(c == '\'') i = buildString(i, '\'', chars, line, row, fileName, tokens);
			
			else if(c == '#') i = buildComment(i, chars);
			else if(c != '\r') {
				error = new LexerError(c, Sequence.from(line, row, 1, fileName));
				break;
			}
		}
		
		tokens.add(new Token("", TokenType.END_OF_FILE, Sequence.from(line, row, 0, fileName)));
		
		return new LexerResult(tokens, text, fileName, error);
	}
	
	private static int buildQuestionMark(int index, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		if(index < chars.length - 1) {
			if(chars[index + 1] == '?') {
				tokens.add(new Token("??", TokenType.DOUBLE_QUESTION_MARK, Sequence.from(line, row, 2, fileName)));
				return index + 1;
			}
			
			if(chars[index + 1] == '.') {
				tokens.add(new Token("?.", TokenType.OPTIONAL_CHAINING, Sequence.from(line, row, 2, fileName)));
				return index + 1;
			}
		}
		tokens.add(new Token("?", TokenType.QUESTION_MARK, Sequence.from(line, row, 1, fileName)));
		return index;
	}
	
	private static int buildEqual(int index, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		if(index < chars.length - 1) {
			if(chars[index + 1] == '=') {
				tokens.add(new Token("==", TokenType.DOUBLE_EQUALS, Sequence.from(line, row, 2, fileName)));
				return index + 1;
			}
			
			if(chars[index + 1] == '>') {
				tokens.add(new Token("=>", TokenType.ARROW, Sequence.from(line, row, 2, fileName)));
				return index + 1;
			}
		}
		tokens.add(new Token("=", TokenType.EQUALS, Sequence.from(line, row, 1, fileName)));
		return index;
	}
	
	private static int buildString(int index, char begchar, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		String str = "";
		boolean escapingNext = false;
		
		for(int i = index+1; i < chars.length; i++) {
			char c = chars[i];
			
			if(escapingNext) {
				str+=STRING_ESCAPE_CHARS.getOrDefault(c, c+"");
				escapingNext = false;
				continue;
			}
			
			if(c == '\\') {
				escapingNext = true;
				continue;
			}
			
			if(c == begchar || c == '\n') {
				
				if(begchar == '\'' && str.length() == 1) {
					tokens.add(new Token(str, TokenType.CHARACTER, Sequence.from(line, row, str.length(), fileName)));
				} else tokens.add(new Token(str, TokenType.STRING, Sequence.from(line, row, str.length(), fileName)));
				
				return i;
			} else str+=c;
			escapingNext = false;
		}
		
		return chars.length;
	}
	
	private static int buildNumber(int index, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		
		if(index < chars.length - 2 && chars[index] == '0') {
			if(chars[index + 1] == 'b') {
				index += 2;
				String bits = "";
				for(; index < chars.length; index++) {
					if(chars[index] == '0' || chars[index] == '1') bits += chars[index];
					else break;
				}
				index--;
				if(bits.length() <= 32) { // creating an int
					int result = 0;
					for(int i = 0; i < bits.length(); i++) {
						result <<= 1;
						
						if(bits.charAt(i) == '1')
							result |= 1;
					}
					tokens.add(new Token(result + "", TokenType.INTEGER, Sequence.from(line, row, bits.length() + 2, fileName)));
				} else { // creating a long
					long result = 0;
					for(int i = 0; i < bits.length(); i++) {
						result <<= 1;
						
						if(bits.charAt(i) == '1')
							result |= 1;
					}
					tokens.add(new Token(result + "", TokenType.LONG, Sequence.from(line, row, bits.length() + 2, fileName)));
				}
				return index;
			} else if(chars[index + 1] == 'x') {
				index += 2;
				String hex = "";
				for(; index < chars.length; index++) {
					if(HEX_CHARS.contains(chars[index]+"")) hex += chars[index];
					else break;
				}
				index--;
				
				int result = Integer.parseInt(hex, 16);
				tokens.add(new Token(result + "", TokenType.INTEGER, Sequence.from(line, row, hex.length() + 2, fileName)));
				return index;
			}
		}
		
		return buildNumber(index, chars, line, row, fileName, tokens, "", false);
	}
	
	private static int buildNumber(int index, char[] chars, int line, int row, String fileName, List<Token> tokens, String precomp, boolean containsPoint) {
		String str = precomp;
		boolean hasAPoint = containsPoint;
		
		for(; index < chars.length; index++) {
			char c = chars[index];
			if(DIGITS.contains(c + "")) str += c;
			else if(c == '.' && !hasAPoint) {
				hasAPoint = true;
				str += '.';
			} else {
				index--;
				break;
			}
		}
		
		TokenType type = hasAPoint?TokenType.DOUBLE:TokenType.INTEGER;
		
		if(index < chars.length - 1) {
			final char next = chars[index + 1];
			
			if(next == 'f' || next == 'F') {
				index++;
				type = TokenType.FLOAT;
			} else if(next == 'd' || next == 'D') {
				index++;
				type = TokenType.FLOAT;
			} else if(!hasAPoint && (next == 'l' || next == 'L')) {
				index++;
				type = TokenType.LONG;
			}
		}
		
		tokens.add(new Token(str, type, Sequence.from(line, row, str.length(), fileName)));
		return index;
	}
	
	private static int buildIdentifier(int index, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		String str = "";
		
		for(; index < chars.length; index++) {
			char c = chars[index];
			if(IDENTIFIER_CHARS.contains(c + "")) str += c;
			else {
				index--;
				break;
			}
		}
		
		tokens.add(new Token(str, isKeyword(str) ? TokenType.KEYWORD : TokenType.IDENTIFIER, Sequence.from(line, row, str.length(), fileName)));
		return index;
	}
	
	private static int buildBinaryOP(int index, char current, TokenType single, TokenType whenEqual, TokenType whenDouble, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		if(index < chars.length - 1) {
			if(chars[index + 1] == '=') {
				tokens.add(new Token(current + "=", whenEqual, Sequence.from(line, row, 2, fileName)));
				return index + 1;
			}
			
			if(chars[index + 1] == current) {
				tokens.add(new Token(current + "" + current, whenDouble, Sequence.from(line, row, 2, fileName)));
				return index + 1;
			}
			
			if(current == '-' && chars[index + 1] == '>') {
				tokens.add(new Token(current + ">", TokenType.ARROW, Sequence.from(line, row, 2, fileName)));
				return index + 1;
			}
		}
		tokens.add(new Token(current + "", single, Sequence.from(line, row, 1, fileName)));
		return index;
	}
	
	private static int buildWhenNextCharacterIs(int index, char current, char other, TokenType single, TokenType next, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		if(index < chars.length - 1 && chars[index + 1] == other) {
			tokens.add(new Token(current + "" + other, next, Sequence.from(line, row, 2, fileName)));
			return index + 1;
		}
		tokens.add(new Token(current + "", single, Sequence.from(line, row, 1, fileName)));
		return index;
	}
	
	private static int buildPoint(int index, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		if(index < chars.length - 1 && DIGITS.contains(chars[index + 1]+"")) {
			index++;
			return buildNumber(index, chars, line, row, fileName, tokens, "0.", true);
		}
		tokens.add(new Token(".", TokenType.POINT, Sequence.from(line, row, 1, fileName)));
		return index;
	}
	
	private static int buildExclamationMark(int index, char[] chars, int line, int row, String fileName, List<Token> tokens) {
		if(index < chars.length - 1 && chars[index + 1] == '=') {
			tokens.add(new Token("!=", TokenType.NOT_EQUAL, Sequence.from(line, row, 2, fileName)));
			return index + 1;
		}
		tokens.add(new Token("not", TokenType.KEYWORD, Sequence.from(line, row, 1, fileName)));
		return index;
	}
	
	private static int buildBooleanOp(int index, char current, char[] chars, String content, int line, int row, String fileName, List<Token> tokens) {
		if(index < chars.length - 1 && chars[index + 1] == current) {
			tokens.add(new Token(content, TokenType.KEYWORD, Sequence.from(line, row, 2, fileName)));
			return index + 1;
		}
		tokens.add(new Token(content, TokenType.KEYWORD, Sequence.from(line, row, 1, fileName)));
		return index;
	}
	
	private static int buildComment(int index, char[] chars) {
		while(true) {
			index++;
			if(index >= chars.length) break;
			if(chars[index] == '\n') return index - 1;
		}
		return index;
	}

	public static boolean isKeyword(String str) {
		for(String keyword:KEYWORDS)
			if(keyword.equals(str))
				return true;
		return false;
	}
	
}
