package holo.interpreter.nodes.structures;

import java.util.HashMap;
import java.util.Map;

import holo.errors.AlreadyExistingVariableError;
import holo.errors.ConstructorNotFoundError;
import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.ClassDeclarationBody;
import holo.interpreter.nodes.helpers.EnumEntry;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.FunctionValue;
import holo.interpreter.values.objects.EnumClassValue;
import holo.interpreter.values.objects.EnumEntryValue;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record EnumDeclarationNode(String name, EnumEntry[] entries, ClassDeclarationBody body, Sequence sequence) implements Node {

	@Override
	public String toString() {
		return "enum " + name + " {\n"+body.toString().indent(4)+"}";
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		if(parentContext.contains(name))
			throw new TError(new AlreadyExistingVariableError(name, sequence));
		
		final Map<String, EnumEntryValue> entriesValue = new HashMap<>();
		
		EnumClassValue enumClassValue = new EnumClassValue(name, parentContext, entriesValue);
		
		FunctionValue[] constructors = new FunctionValue[body.constructors().length];
		
		for(int i = 0; i < constructors.length; i++) {
			Value constructorValue = body.constructors()[i].interpret(enumClassValue, interpreter);
			if(constructorValue instanceof FunctionValue fn)
				constructors[i] = fn;
			else throw new TError(new RuntimeError("Expecting a function value for constructor " + body.constructors()[i], sequence));
		}
		
		for(Node staticNode:body.staticDeclarations())
			staticNode.interpret(enumClassValue, interpreter);
		
		for(int ordinal = 0; ordinal < entries.length; ordinal++) {
			EnumEntry entry = entries[ordinal];
			
			EnumEntryValue entryValue = new EnumEntryValue(name, enumClassValue, entry.name(), ordinal);
			entriesValue.put(entry.name(), entryValue);
			
			FunctionValue choosenConstructor = null;
			for(FunctionValue con:constructors) {
				if(con.numberOfArguments() == entry.args().length) {
					choosenConstructor = con;
					break;
				}
			}
			
			if(choosenConstructor == null && (constructors.length != 0 || entry.args().length > 0))
				throw new TError(new ConstructorNotFoundError(name, entry.args().length, null));
			
			for(Node node:body.instanciateBody().statements())
				node.interpret(entryValue, interpreter);
			
			Value[] args = new Value[entry.args().length];
			for(int argIndex = 0; argIndex < args.length; argIndex++)
				args[argIndex] = entry.args()[argIndex].interpret(enumClassValue, interpreter);
			
			if(choosenConstructor != null)
				choosenConstructor.callInside(entryValue, entryValue, interpreter, args, null);
		}
		
		parentContext.setToThis(name, enumClassValue);
		
		return enumClassValue;
	}

}
