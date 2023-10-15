package holo.interpreter.contexts;

import holo.interpreter.values.Value;
import holo.interpreter.values.functions.BuiltInFunctionValue;
import holo.interpreter.values.interfaces.IClass;

public interface Context {
	
	public void setToThis(String key, Value value);
	public Value getFromThis(String key);
	public boolean contains(String key);
	
	public Context getParent();
	public String getName();
	public boolean isEnclosed();
	
	public default Value get(String key) {
		final Value value = getFromThis(key);
		
		if(value != null)
			return value;
		
		final Context parent = getParent();
		if(parent == null)
			return null;
		
		return parent.get(key);
	}
	
	public default Context getFirstParentThatHasKey(String key) {
		if(contains(key)) return this;
		
		final Context parent = getParent();
		return parent == null ? null : parent.getFirstParentThatHasKey(key);
	}
	
	public default Context getFirstThisableParent() {
		if(thisAble()) return this;
		Context parent = getParent();
		if(parent != null) return parent.getFirstThisableParent();
		return null;
	}
	
	public default Context getFirstSuperableParent() {
		if(superAble()) return this;
		Context parent = getParent();
		if(parent != null) return parent.getFirstSuperableParent();
		return null;
	}
	
	public default boolean thisAble() { return false; }
	public default Value thisValue() { return null; }
	public default boolean superAble() { return false; }
	public default IClass superValue() { return null; }
	
	public default int getDepth() {
		Context parent = getParent();
		if(parent == null)
			return 0;
		return parent.getDepth() + 1;
	}
	
	public default void addBuiltInFunction(BuiltInFunctionValue function) {
		setToThis(function.getName(), function);
	}
	
	public default boolean authorizeNewVar(String name) {
		if(contains(name)) return false;
		
		if(isEnclosed())
			return true;
		
		final Context parent = getParent();
		if(parent == null)
			return true;
		
		return parent.authorizeNewVar(name);
	}
	
	public default String tree() {
		Context parent = getParent();
		if(parent == null) return toString();
		
		return parent.tree() + "\n" + ("L " + toString()).indent(getDepth() * 2);
	}
	
}
