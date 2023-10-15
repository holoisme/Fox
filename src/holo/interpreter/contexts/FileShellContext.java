package holo.interpreter.contexts;

import java.util.ArrayList;
import java.util.List;

import holo.interpreter.imports.Library;

public class FileShellContext extends SimpleContext implements Library {
	
	private List<String> importedLibrariesNames;
	private FileInnerContext innerContext;
	
	public FileShellContext(String fileName) {
		super(fileName, null, true);
		this.importedLibrariesNames = new ArrayList<>();
		this.innerContext = new FileInnerContext("Inner "+fileName, this);
	}
	
	public void addLibrary(Library library) {
		if(importedLibrariesNames.contains(library.name()))
			return;
		library.populate(this);
		importedLibrariesNames.add(library.name());
	}

	public void populate(Context fileContext) {
		// dump self context into fileContext
		System.err.println("Didn't implement File Context dumping");
	}

	public String name() {
		return getName();
	}
	
	public String toString() {
		return getName() + " with libraries " + importedLibrariesNames + " " + table;
	}
	
	public FileInnerContext getInnerContext() { return innerContext; }

}
