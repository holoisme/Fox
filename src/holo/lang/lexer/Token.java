package holo.lang.lexer;

public record Token(String content, TokenType type, Sequence sequence) {
	
	public boolean matches(TokenType type) {
		return this.type.equals(type);
	}
	
	public boolean matches(String content) {
		return this.content.equalsIgnoreCase(content);
	}
	
	public boolean matches(String content, TokenType type) {
		return this.content.equalsIgnoreCase(content) && this.type.equals(type);
	}
	
	public boolean matches(TokenType... types) {
		for(TokenType t:types)
			if(this.type.equals(t))
				return true;
		
		return false;
	}
	
	public boolean matches(String... contents) {
		for(String c:contents)
			if(this.content.equalsIgnoreCase(c))
				return true;
		
		return false;
	}
	
	public String toString() {
		return content;
	}

	public String toStringFull() {
		return type.name() + "("+content+")";
	}
	
}
