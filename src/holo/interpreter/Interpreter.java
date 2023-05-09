package holo.interpreter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import holo.errors.FoxError;
import holo.errors.IOError;
import holo.errors.ImportError;
import holo.interpreter.contexts.FileInnerContext;
import holo.interpreter.contexts.FileShellContext;
import holo.interpreter.imports.Library;
import holo.lang.lexer.Lexer;
import holo.lang.lexer.Token;
import holo.lang.parser.ParseResult;
import holo.lang.parser.Parser;

public class Interpreter {
	
	private final Path rootPath;
	private final Map<String, Library> loadedLibraries;
	private final Map<Path, FileInnerContext> loadedFiles;
	
	private PrintStream outStream, errStream;
	
	private boolean debug = false, performance = false;
	
	public Interpreter(Path rootPath) {
		this.rootPath = rootPath;
		this.loadedLibraries = new HashMap<>();
		this.loadedFiles = new HashMap<>();
		
		this.outStream = System.out;
		this.errStream = System.err;
	}
	
	public FileInnerContext getImportedFile(String path) {
		Path p = rootPath.resolve(path);
		
		if(!loadedFiles.containsKey(p))
			execute(p);
		
		return loadedFiles.get(p);
	}
	
	public Library getBuiltInLibrary(String name) {
		return Library.BUILT_IN_LIBRARIES.get(name);
	}
	
	public Library getLibrary(String name) {
		if(!loadedLibraries.containsKey(name))
			loadLibrary(name);
		
		return loadedLibraries.get(name);
	}
	
	public RuntimeResult execute(String path) {
		return execute(rootPath.resolve(path));
	}
	
	public RuntimeResult execute(Path path) {
		try {
			List<Token> tokens = Lexer.extract(path.toFile());
			Parser parser = new Parser(tokens);
			ParseResult result = parser.parse();
			
			FoxError error = result.getError();
			if(error != null) {
				error.display();
				return new RuntimeResult().failure(new ImportError(error));
			}
			
			RuntimeResult runtime = null;
			
			if(!performance) {
				FileShellContext fileContext = new FileShellContext(path.getFileName().toString());
				
				long startTime = System.currentTimeMillis();
				printDebug("[Interpreter] Executing " + path.toFile().getName() + "...");
				runtime = result.node().interpret(fileContext.getInnerContext(), this, null);
				printDebug("[Interpreter] Executed in " + (System.currentTimeMillis() - startTime) + "ms");
				loadedFiles.put(path, fileContext.getInnerContext());
			} else {
				long timeSum = 0;
				final int iterationCount = 100;
				
				for(int i = 0; i < iterationCount; i++) {
					FileShellContext fileContext = new FileShellContext(path.getFileName().toString());
					
					long startTime = System.currentTimeMillis();
					printDebug("[Interpreter] Executing " + path.toFile().getName() + "...");
					runtime = result.node().interpret(fileContext.getInnerContext(), this, null);
					printDebug("[Interpreter] Executed in " + (System.currentTimeMillis() - startTime) + "ms");
					
					timeSum += (System.currentTimeMillis() - startTime);
					
					loadedFiles.put(path, fileContext.getInnerContext());
				}
				
				print("Average execution time " + (timeSum / (float)iterationCount) + " ms");
			}
			
			if(debug) System.out.println("\n" + runtime.toStringFull(path.toFile().getName()));
			
			if(runtime.hasError())
				runtime.getError().display();
			
			return runtime;
		} catch (IOException e) {
			e.printStackTrace();
			return new RuntimeResult().failure(new IOError(e.getMessage(), null));
		}
	}
	
	public void loadLibrary(String name) {
		// Class Loader stuff
	}
	
	public void print(Object o) {
		outStream.println(o);
	}
	
	public void printDebug(Object o) {
		if(debug)
			outStream.println(o);
	}
	
	public void printErr(Object o) {
		errStream.println(o);
	}
	
	public void setDebug(boolean debug) { this.debug = debug; }
	public void setPerformance(boolean performance) { this.performance = performance; }
	
	public boolean onDebug() { return debug; }
	public boolean onPerformance() { return performance; }
	
}
