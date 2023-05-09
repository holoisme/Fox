package holo.interpreter.contexts;

public class FileInnerContext extends SimpleContext {

	public FileInnerContext(String name, FileShellContext shell) {
		super(name, shell);
	}
	
	public void dumpInto(FileShellContext shell) {
		shell.table.putAll(table);
	}
	
	public FileShellContext getShell() {
		return (FileShellContext) getParent();
	}

}
