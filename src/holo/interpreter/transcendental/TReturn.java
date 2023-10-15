package holo.interpreter.transcendental;

import holo.interpreter.values.Value;

public class TReturn extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final Value value;
	public TReturn(Value value) { this.value = value; }
	
	public Value value() { return value; }
	
	@Override
    public Throwable fillInStackTrace() {
        return this;
    } 
}
