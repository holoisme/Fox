package holo.interpreter.values;

import holo.errors.RuntimeError;
import holo.interpreter.values.primitives.StringValue;

public class ErrorValue implements Value {
	
	protected RuntimeError error;
	
	public ErrorValue(RuntimeError error) {
		this.error = error;
	}
	
	public RuntimeError getError() { return error; }
	
	@Override
	public String typeName() {
		return error.getName();
	}
	
	public String toString() {
		return error.textToDisplay();
	}

	@Override
	public boolean isTrue() {
		return true;
	}

	@Override
	public boolean equalTo(Value other) {
		if(other instanceof ErrorValue ev)
			return error.equals(ev.error);
		else if(other instanceof StringValue sv)
			return error.getName().equals(sv.getValue());
		
		return false;
	}

	@Override
	public Value pointGet(String key) {
		// TODO Auto-generated method stub
		
		return null/*ErrorPrototype.PROTOTYPE.get(key)*/;
	}

	public Value pointSet(String key, Value value) {
		return null;
	}

	public Value arrayGet(Value key) {
		return null;
	}

	public Value arraySet(Value key, Value value) {
		return null;
	}

}
