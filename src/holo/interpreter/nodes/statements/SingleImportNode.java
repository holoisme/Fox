package holo.interpreter.nodes.statements;

import holo.errors.ImportError;
import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.FileInnerContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record SingleImportNode(String path, Sequence sequence) implements Node {
	
	public String toString() {
		return "import " + path;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		
		if(parentContext instanceof FileInnerContext fileContext) {
			FileInnerContext imported = interpreter.getImportedFile(path);
			if(imported == null)
				throw new TError(new ImportError(path, sequence));
			
			imported.dumpInto(fileContext.getShell());
//			Library library = builtIn ? interpreter.getBuiltInLibrary(name) : interpreter.getLibrary(name);
//			if(library == null) return rt.failure(new RuntimeError("Could not import" + (builtIn?" the built in":"") +  " library " + name, sequence));
			
//			fileContext.getShell().addLibrary(library);
			
			return Value.NULL;
		} else throw new TError(new RuntimeError("Cannot import in inner context", sequence));
		
	}
}