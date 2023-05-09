package holo.interpreter.contexts;

import holo.interpreter.values.Value;
import holo.interpreter.values.functions.BuiltInFunctionValue;

public interface Context {
	
	public void setToThis(String key, Value value);
	public Value getFromThis(String key);
	public boolean contains(String key);
	
	public Context getParent();
	public String getName();
	
	public default Value get(String key) {
		Value value = getFromThis(key);
		
		if(value != null)
			return value;
		
		Context parent = getParent();
		if(parent == null)
			return null;
		
		return parent.get(key);
	}
	
	public Context getFirstParentThatHasKey(String key);
	
	public default Context getFirstThisableParent() {
		if(thisAble()) return this;
		Context parent = getParent();
		if(parent != null) return parent.getFirstThisableParent();
		return null;
	}
	
	public default boolean thisAble() { return false; }
	public default Value thisValue() { return null; }
	
	public default int getDepth() {
		Context parent = getParent();
		if(parent == null)
			return 0;
		return parent.getDepth() + 1;
	}
	
	public default void addBuiltInFunction(BuiltInFunctionValue function) {
		setToThis(function.getName(), function);
	}
	
	public default String tree() {
		Context parent = getParent();
		if(parent == null) return toString();
		
		return parent.tree() + "\n" + ("L " + toString()).indent(getDepth() * 2);
	}
	
}
