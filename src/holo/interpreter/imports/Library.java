package holo.interpreter.imports;

import java.util.Map;

import holo.interpreter.contexts.Context;

public interface Library {
	
	static final Map<String, Library> BUILT_IN_LIBRARIES = Map.ofEntries(
		Map.entry("standard", new StandardLibrary())
	);
	
	public void populate(Context fileContext);
	public String name();
	
}
