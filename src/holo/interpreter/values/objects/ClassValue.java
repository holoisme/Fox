package holo.interpreter.values.objects;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import holo.errors.ConstructorNotFoundError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.structures.MultiStatementsNode;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.FunctionValue;
import holo.interpreter.values.interfaces.IInstanciable;
import holo.lang.lexer.Sequence;

public class ClassValue implements Value, Context, IInstanciable {
	
	private String name;
	private Context definingContext;
	
	private FunctionValue[] constructors;
	private MultiStatementsNode instanciateBody;
	
	protected Map<String, Value> staticTable;
	
	public ClassValue(String name, Context definingContext, FunctionValue[] constructors, MultiStatementsNode instanciateBody) {
		this.name = name;
		this.definingContext = definingContext;
		
		this.staticTable = new HashMap<>();
		this.constructors = constructors;
		this.instanciateBody = instanciateBody;
	}
	
	public String typeName() {
		return "class";
	}

	public boolean isTrue() { return true; }

	public Value pointGet(String key) {
		return staticTable.get(key);
	}
	
	public Value pointSet(String key, Value value) {
		staticTable.put(key, value);
		return value;
	}

	public Value arrayGet(Value key) { return staticTable.get(key.toString()); }
	public Value arraySet(Value key, Value value) { return null; }
	
	public boolean equalTo(Value other) { return this.equals(other); }
	
	public RuntimeResult createInstance(Interpreter interpreter, RuntimeResult onGoing, Sequence sequence, Value... args) {
		FunctionValue choosenConstructor = null;
		for(FunctionValue con:constructors) {
			if(con.numberOfArguments() == args.length) {
				choosenConstructor = con;
				break;
			}
		}
		
		if(choosenConstructor == null && (constructors.length != 0 || args.length > 0))
			return onGoing.failure(new ConstructorNotFoundError(name, args.length, sequence));
		
		ConstructedObjectValue object = new ConstructedObjectValue(name, this);
		
		for(Node node:instanciateBody.statements()) {
			onGoing.register(node.interpret(object, interpreter, onGoing), sequence);
			if(onGoing.shouldReturn()) return onGoing;
		}
		
		if(choosenConstructor != null) {
			onGoing.register(choosenConstructor.callInside(object, object, interpreter, onGoing, args), sequence);
			if(onGoing.shouldReturn()) return onGoing;
		}
		
		return onGoing.buffer(object);
	}

	@Override
	public void setToThis(String key, Value value) {
		staticTable.put(key, value);
	}

	@Override
	public Value getFromThis(String key) {
		return staticTable.get(key);
	}

	@Override
	public boolean contains(String key) {
		return staticTable.containsKey(key);
	}

	@Override
	public Context getParent() {
		return definingContext;
	}

	@Override
	public String getName() {
		return "class " + name;
	}

	public Context getFirstParentThatHasKey(String key) {
		if(staticTable.containsKey(key)) return this;
		return definingContext.getFirstParentThatHasKey(key);
	}
	
	public String toString() {
		String str = "class " + name + "{\n";
		if(staticTable.size() != 0)
			for(Entry<String, Value> entry : staticTable.entrySet())
				str += ("static var "+entry.getKey() + " = "+entry.getValue()).indent(4) + "\n";
		if(constructors.length != 0)
			for(FunctionValue c:constructors)
				str += ("constructor"+c.toString()).indent(4) + "\n";
		return str + instanciateBody.toString().indent(4) + "}";
	}

}
