package holo.lang.parser;

public class TParseFail extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public static final TParseFail FAILURE = new TParseFail();
	
	private TParseFail() {}
	
	@Override
    public Throwable fillInStackTrace() {
        return this;
    } 
	
}