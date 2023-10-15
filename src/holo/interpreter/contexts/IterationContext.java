package holo.interpreter.contexts;

public class IterationContext extends SimpleContext {

	public IterationContext(Context TRUE_PARENT) {
		super("iteration", TRUE_PARENT, false);
	}
	
	public void clear() {
		table.clear();
	}

}
