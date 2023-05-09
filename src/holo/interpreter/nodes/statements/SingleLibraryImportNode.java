package holo.interpreter.nodes.statements;

import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.FileInnerContext;
import holo.interpreter.imports.Library;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record SingleLibraryImportNode(String name, boolean builtIn, Sequence sequence) implements Node {
	
	public String toString() {
		return "library " + name;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		if(parentContext instanceof FileInnerContext fileContext) {
			Library library = builtIn ? interpreter.getBuiltInLibrary(name) : interpreter.getLibrary(name);
			if(library == null) return rt.failure(new RuntimeError("Could not import" + (builtIn?" the built in":"") +  " library " + name, sequence));
			
			fileContext.getShell().addLibrary(library);
			
			return rt.success(Value.NULL);
		} else return rt.failure(new RuntimeError("Cannot import in inner context", sequence));
		
	}
	
}
