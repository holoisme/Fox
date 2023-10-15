package holo.interpreter.values.objects;

import java.util.HashMap;
import java.util.Map;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.ICallHandler;
import holo.interpreter.values.interfaces.IClass;
import holo.lang.lexer.Sequence;

public class ConstructedObjectValue implements Value, Context, ICallHandler {
	
	protected IClass classValue;
	protected Map<String, Value> fields;
	
	public ConstructedObjectValue(IClass classValue) {
		this.classValue = classValue;
		this.fields = new HashMap<>();
	}
	
	public Value binaryOperation(BinaryOperationType operation, Value right, Interpreter interpreter, Sequence sequence) {
		return classValue.executeBinaryOperation(operation, this, right, this, interpreter, sequence);
//		final String fnName = "$"+operation.getName();
//		if(!classValue.containsFunction(fnName))
//			return Value.super.binaryOperation(operation, right, interpreter, sequence);
//		return classValue.executeFunction(fnName, this, this, new Value[] { right }, null, interpreter, sequence);
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
	public Value pointGet(String key, Sequence sequence) {
		Value value = fields.get(key);
		if(value == null)
			return Value.super.pointGet(key, sequence);
		return value;
	}

	@Override
	public Value pointSet(String key, Value value, Sequence sequence) {
		fields.put(key.toString(), value);
		return value;
	}

	@Override
	public Value arrayGet(Value key, Sequence sequence) {
		return fields.get(key.toString());
	}

	@Override
	public Value arraySet(Value key, Value value, Sequence sequence) {
		fields.put(key.toString(), value);
		return value;
	}
	
	@Override
	public String typeName() { return classValue.getClassName(); }
	
	@Override
	public Value typeOf() { return classValue.instanceTypeOf(); }

	@Override
	public void setToThis(String key, Value value) {
		fields.put(key, value);
	}

	@Override
	public Value getFromThis(String key) {
		return fields.get(key);
	}

	@Override
	public boolean contains(String key) {
		return fields.containsKey(key) || classValue.containsFunction(key);
	}

	@Override
	public Context getParent() {
		return (Context) classValue;
	}

	@Override
	public String getName() {
		return classValue.getClassName() + " object";
	}
	
	public String toString(Interpreter interpreter, Sequence sequence) {
		if(classValue.containsFunction("toString"))
			return classValue.executeFunction("toString", this, this, null, null, interpreter, sequence).toString();
		return toString();
	}
	
	public String toString() {
		return getName() + " " + fields;
	}
	
	@Override
	public boolean thisAble() {
		return true;
	}
	
	@Override
	public Value thisValue() {
		return this;
	}

	@Override
	public boolean isEnclosed() {
		return true;
	}

	@Override
	public Value callMethod(String name, Value[] args, NamedValue[] optionalArguments, Interpreter interpreter, Sequence sequence) {
		return classValue.executeFunction(name, this, this, args, optionalArguments, interpreter, sequence);
	}
	
	public boolean superAble() { return classValue.getSuperClass() != null; }
	public IClass superValue() { return classValue.getSuperClass(); }

}
