package holo.interpreter.nodes.structures;

import java.util.HashMap;
import java.util.Map;

import holo.errors.AlreadyExistingVariableError;
import holo.errors.ConstructorNotFoundError;
import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.ClassDeclarationBody;
import holo.interpreter.nodes.helpers.EnumEntry;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.FunctionValue;
import holo.interpreter.values.objects.EnumClassValue;
import holo.interpreter.values.objects.EnumEntryValue;
import holo.lang.lexer.Sequence;

public record EnumDeclarationNode(String name, EnumEntry[] entries, ClassDeclarationBody body, Sequence sequence) implements Node {

	@Override
	public String toString() {
		return "enum " + name + " {\n"+body.toString().indent(4)+"}";
	}
	
	@Override
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		if(parentContext.contains(name))
			return onGoingRuntime.failure(new AlreadyExistingVariableError(name, sequence));
		
		final Map<String, EnumEntryValue> entriesValue = new HashMap<>();
		
		EnumClassValue enumClassValue = new EnumClassValue(name, parentContext, entriesValue);
		
		FunctionValue[] constructors = new FunctionValue[body.constructors().length];
		
		for(int i = 0; i < constructors.length; i++) {
			Value constructorValue = onGoingRuntime.register(body.constructors()[i].interpret(enumClassValue /*TODO verify*/, interpreter, onGoingRuntime), body.constructors()[i].sequence());
			if(constructorValue instanceof FunctionValue fn)
				constructors[i] = fn;
			else return onGoingRuntime.failure(new RuntimeError("Expecting a function value for constructor " + body.constructors()[i], sequence));
		}
		
		for(Node staticNode:body.staticDeclarations()) {
			onGoingRuntime.register(staticNode.interpret(enumClassValue, interpreter, onGoingRuntime), staticNode.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		
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
				return onGoingRuntime.failure(new ConstructorNotFoundError(name, entry.args().length, null));
			
			for(Node node:body.instanciateBody().statements()) {
				onGoingRuntime.register(node.interpret(entryValue, interpreter, onGoingRuntime), entry.sequence());
				if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			}
			
			Value[] args = new Value[entry.args().length];
			for(int argIndex = 0; argIndex < args.length; argIndex++) {
				args[argIndex] = onGoingRuntime.register(entry.args()[argIndex].interpret(enumClassValue, interpreter, onGoingRuntime), entry.args()[argIndex].sequence());
				if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			}
			
			if(choosenConstructor != null) {
				onGoingRuntime.register(choosenConstructor.callInside(entryValue, entryValue, interpreter, onGoingRuntime, args), null);
				if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
			}
		}
		
		parentContext.setToThis(name, enumClassValue);
		
		return onGoingRuntime.buffer(null);
	}

}
