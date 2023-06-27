package holo.transcendental;

import holo.errors.RuntimeError;
import holo.lang.lexer.Sequence;

public class TError extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final RuntimeError error;
	public TError(RuntimeError error) { this.error = error; }
	
	public RuntimeError error() { return error; }
	
	public TError addToTrace(String name, Sequence sequence) {
		error.addToTrace(name, sequence);
		return this;
	}
	
	@Override
    public Throwable fillInStackTrace() {
        return this;
    } 
}
