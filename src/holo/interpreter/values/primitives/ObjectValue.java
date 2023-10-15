package holo.interpreter.values.primitives;

import java.util.HashMap;
import java.util.Map;

import holo.errors.NoSuchVariableError;
import holo.interpreter.contexts.Context;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public class ObjectValue implements Value, Context {
	
	private final Map<String, Value> table;
	private final Context definingContext;
	
	public ObjectValue(Context definingContext) {
		this.table = new HashMap<>();
		this.definingContext = definingContext;
	}
	
	public ObjectValue(Map<String, Value> table, Context definingContext) {
		this.table = table;
		this.definingContext = definingContext;
	}
	
	public Map<String, Value> getTable() { return table; }

	@Override
	public boolean isTrue() {
		return !table.isEmpty();
	}

	@Override
	public boolean equalTo(Value other) {
		if(other instanceof ObjectValue ov)
			return table.equals(ov.table);
		return false;
	}

	@Override
	public Value pointGet(String key, Sequence sequence) {
		if(table.containsKey(key))
			return table.get(key);
		throw new TError(new NoSuchVariableError(key, sequence));
	}

	@Override
	public Value pointSet(String key, Value value, Sequence sequence) {
		table.put(key.toString(), value);
		return value;
	}

	@Override
	public Value arrayGet(Value key, Sequence sequence) {
		return table.get(key.toString());
	}

	@Override
	public Value arraySet(Value key, Value value, Sequence sequence) {
		table.put(key.toString(), value);
		return value;
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
	public Context getParent() { return definingContext; }

	@Override
	public String getName() { return "[object]"; }
	
	@Override
	public boolean thisAble() {
		return true;
	}
	
	@Override
	public Value thisValue() {
		return this;
	}

	@Override
	public boolean contains(String key) {
		return table.containsKey(key);
	}
	
	@Override
	public String typeName() { return "object"; }
	
	@Override
	public Value typeOf() { return new StringValue("object"); }
	
	public String toString() {
		return table.toString();
	}

	@Override
	public boolean isEnclosed() {
		return true;
	}
	
}
