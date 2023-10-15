package holo.interpreter.values.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import holo.errors.ConstructorNotFoundError;
import holo.errors.IllegalOperationError;
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
import holo.interpreter.values.interfaces.IInstanciable;
import holo.interpreter.values.primitives.StringValue;
import holo.lang.lexer.Sequence;

public class ClassValue implements Value, Context, IClass, IInstanciable {
	
	private final String name;
	private final Context definingContext;
	private final IClass superClass;
	
	private final ConstructorValue[] constructors;
	private final Map<String, FunctionValue> functions;
	private final Map<BinaryOperationType, OperatorDefinitionNode> operators;
	
	protected final Map<String, Value> staticTable;
	
	public ClassValue(String name, Context definingContext, IClass superClass, ConstructorValue[] constructors, Map<String, FunctionValue> functions, Map<BinaryOperationType, OperatorDefinitionNode> operators) {
		this.name = name;
		this.definingContext = definingContext;
		this.superClass = superClass;
		
		this.staticTable = new HashMap<>();
		this.constructors = constructors;
		this.functions = functions;
		this.operators = operators;
	}
	
	@Override
	public String typeName() {
		return "class";
	}
	
	@Override
	public Value typeOf() { return new StringValue("class"); }

	public boolean isTrue() { return true; }

	public Value pointGet(String key, Sequence sequence) {
		return staticTable.get(key);
	}
	
	public Value pointSet(String key, Value value, Sequence sequence) {
		staticTable.put(key, value);
		return value;
	}

	public Value arrayGet(Value key, Sequence sequence) { return staticTable.get(key.toString()); }
	public Value arraySet(Value key, Value value, Sequence sequence) { return null; }
	
	public boolean equalTo(Value other) { return this.equals(other); }
	
	@Override
	public Value createInstance(Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments) {
		ConstructedObjectValue object = new ConstructedObjectValue(this);
		construct(getConstructor(args.length), object, interpreter, sequence, args, optionalArguments);
		return object;
	}
	
	@Override
	public ConstructorValue getConstructor(int numArgs) {
		for(ConstructorValue con:constructors)
			if(con.numberOfArguments() == numArgs)
				return con;
		return null;
	}
	
	@Override
	public void construct(ConstructorValue choosenConstructor, Context insideContext, Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments) {
		if(choosenConstructor == null && (constructors.length != 0 || args.length > 0))
			throw new TError(new ConstructorNotFoundError(name, args.length, sequence));
		
		if(choosenConstructor != null)
			choosenConstructor.constructInside(this, insideContext, interpreter, sequence, args, optionalArguments);
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
	
	public String getClassName() {
		return name;
	}
	
	@Override
	public boolean containsBinaryOperation(BinaryOperationType operation) {
		return operators.containsKey(operation) || (superClass == null ? false : superClass.containsBinaryOperation(operation));
	}
	
	@Override
	public boolean containsFunction(String key) {
		return functions.containsKey(key) || (superClass == null ? false : superClass.containsFunction(key));
	}
	
	public String toString() {
		String str = "class " + name + "{\n";
		if(staticTable.size() != 0)
			for(Entry<String, Value> entry : staticTable.entrySet())
				str += ("static var "+entry.getKey() + " = "+entry.getValue()).indent(4) + "\n";
		if(constructors.length != 0)
			for(ConstructorValue c:constructors)
				str += ("constructor"+c.toString()).indent(4) + "\n";
		return str + functions.toString().indent(4) + "\n}";
	}

	@Override
	public boolean isEnclosed() {
		return true;
	}

	@Override
	public IClass getSuperClass() { return superClass; }
	
	@Override
	public Value instanceTypeOf() { return this; }

	@Override
	public Value executeFunction(String functionName, Value instance, Context insideContext, Value[] args, NamedValue[] optionalArguments, Interpreter interpreter, Sequence sequence) {
		FunctionValue function = functions.get(functionName);
		if(function == null) {
			if(superClass != null)
				return superClass.executeFunction(functionName, instance, insideContext, args, optionalArguments, interpreter, sequence);
			throw new TError(new NoSuchMethodError(functionName, null, insideContext.getName(), sequence));
		}
		return function.callInside(instance, insideContext, interpreter, sequence, args, optionalArguments);
	}

	@Override
	public Value executeBinaryOperation(BinaryOperationType operation, Value instance, Value argument, Context insideContext, Interpreter interpreter, Sequence sequence) {
		OperatorDefinitionNode operator = operators.get(operation);
		if(operator == null) {
			if(superClass != null)
				return superClass.executeBinaryOperation(operation, instance, argument, insideContext, interpreter, sequence);
			throw new TError(new IllegalOperationError(operation.getName(), getName(), sequence));
		}
		return operator.binaryOperation(argument, insideContext, interpreter, sequence);
	}

}
