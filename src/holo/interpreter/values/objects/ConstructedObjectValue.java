package holo.interpreter.values.objects;

import java.util.HashMap;
import java.util.Map;

import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;

public class ConstructedObjectValue implements Value, Context {
	
	private String className;
	private Context classDefiningContext;
	
	protected Map<String, Value> table;
	
	public ConstructedObjectValue(String className, Context classDefiningContext) {
		this.className = className;
		this.classDefiningContext = classDefiningContext;
		
		this.table = new HashMap<>();
	}
	
	@Override
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		return this.equals(other);
	}

	@Override
	public Value pointGet(String key) {
		return table.get(key);
	}

	@Override
	public Value pointSet(String key, Value value) {
		table.put(key.toString(), value);
		return value;
	}

	@Override
	public Value arrayGet(Value key) {
		return table.get(key.toString());
	}

	@Override
	public Value arraySet(Value key, Value value) {
		table.put(key.toString(), value);
		return value;
	}
	
	public String typeName() { return className; }

	@Override
	public void setToThis(String key, Value value) {
		table.put(key, value);
	}

	@Override
	public Value getFromThis(String key) {
		return table.getOrDefault(key, null);
	}

	@Override
	public boolean contains(String key) {
		return table.containsKey(key);
	}

	@Override
	public Context getParent() {
		return classDefiningContext;
	}

	@Override
	public String getName() {
		return className + " object";
	}
	
	public String toString() {
		return getName() + " " + table;
	}

	@Override
	public Context getFirstParentThatHasKey(String key) {
		if(table.containsKey(key)) return this;
		return classDefiningContext.getFirstParentThatHasKey(key);
	}
	
	@Override
	public boolean thisAble() {
		return true;
	}
	
	@Override
	public Value thisValue() {
		return this;
	}

}
