package holo.interpreter.values.primitives;

import java.util.ArrayList;
import java.util.List;

import holo.errors.IndexOutOfBoundsError;
import holo.interpreter.transcendental.TError;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IIterable;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.prototypes.ListPrototype;
import holo.lang.lexer.Sequence;

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
	public Value pointGet(String key, Sequence sequence) {
		return ListPrototype.PROTOTYPE.get(key);
	}

	@Override
	public Value arrayGet(Value key, Sequence sequence) {
		if(key instanceof INumber integer) {
			final int index = integer.getInteger();
			if(index < 0 || index >= elements.size())
				throw new TError(new IndexOutOfBoundsError(index, elements.size(), sequence));
			return elements.get(index);
		}
		return Value.super.arrayGet(key, sequence);
	}

	@Override
	public Value arraySet(Value key, Value value, Sequence sequence) {
		if(key instanceof INumber num)
			elements.set(num.getInteger(), value);
		return NULL; // TODO
	}
	
	@Override
	public String typeName() { return "list"; }
	
	@Override
	public Value typeOf() { return new StringValue("list"); }

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
