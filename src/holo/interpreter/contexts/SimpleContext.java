package holo.interpreter.contexts;

import java.util.HashMap;
import java.util.Map;

import holo.interpreter.values.Value;

public class SimpleContext implements Context {
	
	private final String name;
	private final Context parent;
	
	protected Map<String, Value> table;
	
	public SimpleContext(String name, Context TRUE_PARENT) {
		this.name = name;
		this.parent = TRUE_PARENT;
		this.table = new HashMap<>();
	}
	
	@Override
	public void setToThis(String key, Value value) {
		table.put(key, value);
	}

	@Override
	public Value getFromThis(String key) {
		return table.getOrDefault(key, null);
	}
	
	@Override
	public Context getParent() { return parent; }
	
	@Override
	public String getName() { return name; }

	@Override
	public Context getFirstParentThatHasKey(String key) {
		if(table.containsKey(key)) return this;
		Context parent = getParent();
		return parent == null ? null : parent.getFirstParentThatHasKey(key);
	}

	@Override
	public boolean contains(String key) {
		return table.containsKey(key);
	}
	
	public String toString() {
		return name + " " + table;
	}
	
}
