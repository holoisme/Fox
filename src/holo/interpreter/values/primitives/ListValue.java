package holo.interpreter.values.primitives;

import java.util.ArrayList;
import java.util.List;

import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IIterable;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.prototypes.ListPrototype;

public class ListValue implements Value, IIterable {
	
	private final List<Value> elements;
	
	public ListValue() {
		this.elements = new ArrayList<>();
	}
	
	public ListValue(List<Value> elements) {
		this.elements = elements;
	}
	
	public List<Value> getElements() { return elements; }
	
	public String toString() { return elements.toString(); }

	@Override
	public boolean isTrue() {
		return !elements.isEmpty();
	}

	@Override
	public boolean equalTo(Value other) {
		if(other instanceof ListValue lv)
			return elements.equals(lv.elements);
		return false;
	}

	@Override
	public Value pointGet(String key) {
		return ListPrototype.PROTOTYPE.get(key);
	}

	@Override
	public Value pointSet(String key, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value arrayGet(Value key) {
		if(key instanceof IntegerValue integer)
			return elements.get(integer.getValue());
		return null;
	}

	@Override
	public Value arraySet(Value key, Value value) {
		if(key instanceof INumber num)
			elements.set(num.getInteger(), value);
		return NULL; // TODO
	}
	
	public String typeName() { return "list"; }

	@Override
	public Value elementAt(int index) {
		return elements.get(index);
	}

	@Override
	public boolean hasReachedEnd(int index) {
		return index >= elements.size();
	}

	public int size() {
		return elements.size();
	}

	public Value addElement(Value value) {
		elements.add(value);
		return value;
	}
	
	public Object toJavaObject() { return elements; }
	
}
