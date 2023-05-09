package holo.interpreter.values.objects;

import java.util.HashMap;
import java.util.Map;

import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;
import holo.interpreter.values.prototypes.EnumClassPrototype;

public class EnumClassValue implements Value, Context {
	
	private String name;
	
	private Context definingContext;
	private Map<String, Value> table;
	private Map<String, EnumEntryValue> entries;
	
	public EnumClassValue(String name, Context definingContext, Map<String, EnumEntryValue> entries) {
		this.name = name;
		
		this.definingContext = definingContext;
		
		this.table = new HashMap<>();
		this.entries = entries;
	}

	@Override
	public String typeName() {
		return "enum";
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
		Value entry = entries.get(key);
		return entry == null ? table.getOrDefault(key, EnumClassPrototype.PROTOTYPE.get(key)) : entry;
	}

	@Override
	public Value pointSet(String key, Value value) {
		return null;
	}

	@Override
	public Value arrayGet(Value key) {
		return null;
	}

	@Override
	public Value arraySet(Value key, Value value) {
		return null;
	}

	@Override
	public void setToThis(String key, Value value) {
		table.put(key, value);
	}

	@Override
	public Value getFromThis(String key) {
		Value entry = entries.get(key);
		return entry == null ? table.get(key) : entry;
	}

	@Override
	public boolean contains(String key) {
		return table.containsKey(key) || entries.containsKey(key);
	}

	@Override
	public Context getParent() {
		return definingContext;
	}

	@Override
	public String getName() {
		return "enum " + name;
	}

	@Override
	public Context getFirstParentThatHasKey(String key) {
		if(contains(key)) return this;
		return definingContext.getFirstParentThatHasKey(key);
	}
	
	public Map<String, EnumEntryValue> getEntries() { return entries; }

}
