package holo.interpreter.transcendental;

public class TContinue extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public static final TContinue CONTINUE = new TContinue();
	
	@Override
    public Throwable fillInStackTrace() {
        return this;
    } 
}
