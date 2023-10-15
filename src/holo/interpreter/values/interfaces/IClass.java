package holo.interpreter.values.interfaces;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.ConstructorValue;
import holo.lang.lexer.Sequence;

public interface IClass {
	
	IClass getSuperClass();
	
	Value executeFunction(String functionName, Value instance, Context insideContext, Value[] args, NamedValue[] optionalArguments, Interpreter interpreter, Sequence sequence);
	boolean containsFunction(String functionName);
	Value executeBinaryOperation(BinaryOperationType operation, Value instance, Value argument, Context insideContext, Interpreter interpreter, Sequence sequence);
	boolean containsBinaryOperation(BinaryOperationType operation);
	
	ConstructorValue getConstructor(int numArgs);
	void construct(ConstructorValue constructor, Context insideContext, Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments);

	String getClassName();
	Value instanceTypeOf();
	
//	boolean canExecuteFunction(String functionName);
	
}
