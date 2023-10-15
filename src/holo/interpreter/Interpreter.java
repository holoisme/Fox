package holo.interpreter;

import java.nio.file.Path;

import holo.interpreter.contexts.FileInnerContext;
import holo.interpreter.imports.Library;
import holo.interpreter.values.Value;

public interface Interpreter {
	
	public FileInnerContext getImportedFile(String path);
	public Library getBuiltInLibrary(String name);
	public Library getLibrary(String name);
	public void loadLibrary(String name);
	
	public Path getRootPath();
	
	public Value execute(Path path);
	public default Value execute(String path) {
		return execute(getRootPath().resolve(path));
	}
	
	public void setDebug(boolean debug);
	
	public void print(Object o);
	public void printDebug(Object o);
	public void printErr(Object o);
	
}
