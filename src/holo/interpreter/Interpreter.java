package holo.interpreter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import holo.errors.FoxError;
import holo.errors.IOError;
import holo.errors.ImportError;
import holo.interpreter.contexts.FileInnerContext;
import holo.interpreter.contexts.FileShellContext;
import holo.interpreter.imports.Library;
import holo.lang.lexer.Lexer;
import holo.lang.lexer.LexerResult;
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
			long timer = System.currentTimeMillis();
			LexerResult lexResult = Lexer.extract(path.toFile());
			print("[Lexer] " + (System.currentTimeMillis() - timer) + "ms for " + path.toString() + " ["+lexResult.tokens().size()+" tokens]");
			
			if(lexResult.hasError()) {
				lexResult.error().display(errStream, lexResult.originalText());
				return new RuntimeResult().failure(new ImportError(lexResult.error()));
			}
			
			Parser parser = new Parser(lexResult);
			
			timer = System.currentTimeMillis();
			ParseResult parseResult = parser.parse();
			print("[Parser] " + (System.currentTimeMillis() - timer) + "ms for " + path.toString() + "\n");
			
			FoxError error = parseResult.getError();
			if(error != null) {
				error.display(errStream, lexResult.originalText());
				return new RuntimeResult().failure(new ImportError(error));
			}
			
			FileShellContext fileContext = new FileShellContext(path.getFileName().toString());
			
			printDebug("[Interpreter] Executing " + path.toFile().getName() + "...");
			long startTime = System.currentTimeMillis();
			RuntimeResult runtime = parseResult.node().interpret(fileContext.getInnerContext(), this, null);
			printDebug("[Interpreter] Executed in " + (System.currentTimeMillis() - startTime) + "ms");
			
			loadedFiles.put(path, fileContext.getInnerContext());
			
			printDebug("\n" + runtime.toStringFull(path.toFile().getName()));
			
			if(runtime.hasError())
				runtime.getError().display(errStream, lexResult.originalText());
			
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
	
	protected void addLoadedFile(Path path, FileInnerContext context) { loadedFiles.put(path, context); }
	
	public void setDebug(boolean debug) { this.debug = debug; }
	public void setPerformance(boolean performance) { this.performance = performance; }
	
	public boolean onDebug() { return debug; }
	public boolean onPerformance() { return performance; }
	
	public PrintStream getOutStream() { return outStream; }
	public PrintStream getErrorStream() { return errStream; }
	
}
