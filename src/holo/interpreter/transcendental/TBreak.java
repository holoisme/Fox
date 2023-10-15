package holo.interpreter.transcendental;

public class TBreak extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public static final TBreak BREAK = new TBreak();
	
	@Override
    public Throwable fillInStackTrace() {
        return this;
    } 
}
