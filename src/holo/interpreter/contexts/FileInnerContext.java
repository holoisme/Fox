package holo.interpreter.contexts;

public class FileInnerContext extends SimpleContext {

	public FileInnerContext(String name, FileShellContext shell) {
		super(name, shell, true);
	}
	
	public void dumpInto(FileShellContext shell) {
		shell.table.putAll(table);
	}
	
	public void clear() {
		table.clear();
	}
	
	public FileShellContext getShell() {
		return (FileShellContext) getParent();
	}

}
