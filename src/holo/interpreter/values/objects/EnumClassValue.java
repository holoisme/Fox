package holo.interpreter.values.objects;

import java.util.HashMap;
import java.util.Map;

import holo.errors.IllegalOperationError;
import holo.errors.RuntimeError;
import holo.errors.portal.NoSuchMethodError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.nodes.structures.OperatorDefinitionNode;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.ConstructorValue;
import holo.interpreter.values.functions.FunctionValue;
import holo.interpreter.values.interfaces.IClass;
import holo.interpreter.values.primitives.StringValue;
import holo.interpreter.values.prototypes.EnumClassPrototype;
import holo.interpreter.values.prototypes.EnumEntryPrototype;
import holo.lang.lexer.Sequence;

public class EnumClassValue implements Value, Context, IClass {
	
	private String name;
	private Context definingContext;
	
	private final Map<String, FunctionValue> functions;
	private final Map<BinaryOperationType, OperatorDefinitionNode> operators;
	
	private Map<String, EnumEntryValue> entries;
	private Map<String, Value> staticTable;
	
	public EnumClassValue(String name, Context definingContext, Map<String, EnumEntryValue> entries, Map<String, FunctionValue> functions, Map<BinaryOperationType, OperatorDefinitionNode> operators) {
		this.name = name;
		this.definingContext = definingContext;
		
		this.functions = functions;
		this.operators = operators;
		
//		functions.put("name", EnumEntryPrototype.PROTOTYPE.)
		
		this.staticTable = new HashMap<>();
		this.entries = entries;
	}

	@Override
	public String typeName() { return "enum"; }
	
	@Override
	public Value typeOf() { return new StringValue("enum"); }

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
		Value entry = entries.get(key);
		return entry == null ? staticTable.getOrDefault(key, EnumClassPrototype.PROTOTYPE.get(key)) : entry;
	}
	
	@Override
	public Value pointSet(String key, Value value, Sequence sequence) {
		Value entry = entries.get(key);
		if(entry != null)
			throw new TError(new RuntimeError("Cannot redefine enum entry '"+key+"' in " + name, sequence));
		staticTable.put(key, value);
		return value;
	}
	
	@Override
	public void setToThis(String key, Value value) {
		staticTable.put(key, value);
	}

	@Override
	public Value getFromThis(String key) {
		Value entry = entries.get(key);
		return entry == null ? staticTable.get(key) : entry;
	}

	@Override
	public boolean contains(String key) {
		return staticTable.containsKey(key) || entries.containsKey(key);
	}

	@Override
	public Context getParent() {
		return definingContext;
	}

	@Override
	public String getName() {
		return "enum " + name;
	}

	public Map<String, EnumEntryValue> getEntries() { return entries; }

	@Override
	public boolean isEnclosed() {
		return true;
	}
	
	@Override
	public IClass getSuperClass() { return null; }

	@Override
	public Value executeFunction(String functionName, Value instance, Context insideContext, Value[] args, NamedValue[] optionalArguments, Interpreter interpreter, Sequence sequence) {
		FunctionValue function = functions.get(functionName);
		if(function == null)
			throw new TError(new NoSuchMethodError(functionName, null, insideContext.getName(), sequence));
		return function.callInside(instance, insideContext, interpreter, sequence, args, optionalArguments);
	}

	@Override
	public Value executeBinaryOperation(BinaryOperationType operation, Value instance, Value argument, Context insideContext, Interpreter interpreter, Sequence sequence) {
		OperatorDefinitionNode operator = operators.get(operation);
		if(operator == null)
			throw new TError(new IllegalOperationError(operation.getName(), getName(), sequence));
		return operator.binaryOperation(argument, insideContext, interpreter, sequence);
	}

	@Override
	public boolean containsFunction(String functionName) {
		return functions.containsKey(functionName);
	}

	@Override
	public boolean containsBinaryOperation(BinaryOperationType operation) {
		return operators.containsKey(operation);
	}

	@Override
	public ConstructorValue getConstructor(int numArgs) {
		return null;
	}

	@Override
	public void construct(ConstructorValue constructor, Context insideContext, Interpreter interpreter,
			Sequence sequence, Value[] args, NamedValue[] optionalArguments) {
		
	}

	@Override
	public String getClassName() {
		return name;
	}

	@Override
	public Value instanceTypeOf() {
		return this;
	}
	
	public String toString() { return "enum " + name; }

}
