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
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Lexer;
import holo.lang.lexer.LexerResult;
import holo.lang.parser.ParseResult;
import holo.lang.parser.SimpleParser;

public class SimpleInterpreter implements Interpreter {
	
	private final Path rootPath;
	private final Map<String, Library> loadedLibraries;
	private final Map<Path, FileInnerContext> loadedFiles;
	
	private PrintStream outStream, errorStream;
	
	private boolean debug = false;
	
	public SimpleInterpreter(Path rootPath, PrintStream out, PrintStream error) {
		this.rootPath = rootPath;
		this.loadedLibraries = new HashMap<>();
		this.loadedFiles = new HashMap<>();
		
		this.outStream = out;
		this.errorStream = error;
	}
	
	public SimpleInterpreter(Path rootPath) {
		this(rootPath, System.out, System.err);
	}
	
	@Override
	public FileInnerContext getImportedFile(String path) {
		Path p = rootPath.resolve(path);
		
		if(!loadedFiles.containsKey(p))
			execute(p);
		
		return loadedFiles.get(p);
	}
	
	@Override
	public Library getBuiltInLibrary(String name) {
		return Library.BUILT_IN_LIBRARIES.get(name);
	}
	
	@Override
	public Library getLibrary(String name) {
		if(!loadedLibraries.containsKey(name))
			loadLibrary(name);
		
		return loadedLibraries.get(name);
	}
	
	@Override
	public Value execute(Path path) {
		try {
			long timer = System.currentTimeMillis();
			LexerResult lexResult = Lexer.extract(path.toFile());
			print("[Lexer] " + (System.currentTimeMillis() - timer) + "ms for " + path.toString() + " ["+lexResult.tokens().size()+" tokens]");
			
			if(lexResult.hasError()) {
				lexResult.error().display(errorStream, lexResult.originalText());
				throw new TError(new ImportError(lexResult.error()));
			}
			
			SimpleParser parser = new SimpleParser(lexResult);
			
			timer = System.currentTimeMillis();
			ParseResult parseResult = parser.parse();
			print("[Parser] " + (System.currentTimeMillis() - timer) + "ms for " + path.toString() + "\n");
			
			FoxError error = parseResult.error();
			if(error != null) {
				error.display(errorStream, lexResult.originalText());
				throw new TError(new ImportError(error));
			}
			
			FileShellContext fileContext = new FileShellContext(path.getFileName().toString());
			
			try {
				printDebug("[Interpreter] Executing " + path.toFile().getName() + "...");
				long startTime = System.currentTimeMillis();
				Value value = parseResult.node().interpret(fileContext.getInnerContext(), this);
				printDebug("[Interpreter] Executed in " + (System.currentTimeMillis() - startTime) + "ms");
				
				loadedFiles.put(path, fileContext.getInnerContext());
				
				return value;
			} catch(TError t) {
				t.error().display(errorStream, lexResult.originalText());
				throw t;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new TError(new IOError(e.getMessage(), null));
		}
	}
	
	@Override
	public void loadLibrary(String name) {
		// Class Loader stuff
	}
	
	@Override
	public void print(Object o) {
		outStream.println(o);
	}
	
	@Override
	public void printDebug(Object o) {
		if(debug)
			outStream.println(o);
	}
	
	@Override
	public void printErr(Object o) {
		errorStream.println(o);
	}
	
	public void clearLoadedFiles() {
		loadedFiles.clear();
	}
	
	protected void addLoadedFile(Path path, FileInnerContext context) { loadedFiles.put(path, context); }
	
	public void setDebug(boolean debug) { this.debug = debug; }
	public boolean onDebug() { return debug; }
	
	public PrintStream getOutStream() { return outStream; }
	public PrintStream getErrorStream() { return errorStream; }

	@Override
	public Path getRootPath() { return rootPath; }
	
}
