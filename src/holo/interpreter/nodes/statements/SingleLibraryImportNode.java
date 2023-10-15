package holo.interpreter.nodes.statements;

import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.FileInnerContext;
import holo.interpreter.imports.Library;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record SingleLibraryImportNode(String name, boolean builtIn, Sequence sequence) implements Node {
	
	public String toString() {
		return "library " + name;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		if(parentContext instanceof FileInnerContext fileContext) {
			final Library library = builtIn ? interpreter.getBuiltInLibrary(name) : interpreter.getLibrary(name);
			if(library == null)
				throw new TError(new RuntimeError("Could not import" + (builtIn?" the built in":"") +  " library " + name, sequence));
			
			fileContext.getShell().addLibrary(library);
			
			return Value.UNDEFINED;
		} else throw new TError(new RuntimeError("Cannot import in inner context", sequence));
	}
	
}
