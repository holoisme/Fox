package holo.interpreter.contexts;

import java.util.HashMap;
import java.util.Map;

import holo.interpreter.values.Value;

public class SimpleContext implements Context {
	
	private final String name;
	private final Context parent;
	private final boolean enclosed;
	
	protected Map<String, Value> table;
	
	public SimpleContext(String name, Context TRUE_PARENT, boolean enclosed) {
		this.name = name;
		this.parent = TRUE_PARENT;
		this.table = new HashMap<>();
		this.enclosed = enclosed;
	}
	
	@Override
	public void setToThis(String key, Value value) {
		table.put(key, value);
	}

	@Override
	public Value getFromThis(String key) {
		return table.get(key);
	}
	
	@Override
	public Context getParent() { return parent; }
	
	@Override
	public String getName() { return name; }
	
	@Override
	public boolean isEnclosed() {
		return enclosed;
	}

	@Override
	public boolean contains(String key) {
		return table.containsKey(key);
	}
	
	public String toString() {
		return name + " " + table;
	}

}
