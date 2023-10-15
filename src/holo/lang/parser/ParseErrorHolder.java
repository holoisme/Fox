package holo.lang.parser;

import holo.errors.FoxError;

public class ParseErrorHolder extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final FoxError error;
	public ParseErrorHolder(FoxError error) { this.error = error; }
	
	public FoxError error() { return error; }
	
	@Override
    public Throwable fillInStackTrace() {
        return this;
    } 
	
}