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
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.ConstructorValue;
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
	public Value interpret(Context parentContext, Interpreter interpreter) {
		if(parentContext.contains(name))
			throw new TError(new AlreadyExistingVariableError(name, sequence));
		
		final Map<String, EnumEntryValue> entriesValue = new HashMap<>();
		
		final Map<String, FunctionValue> functions = new HashMap<>();
		for(FunctionDefinitionNode fun:body.functions())
			functions.put(fun.name(), (FunctionValue) fun.functionExpression().interpret(parentContext, interpreter));
		
		final Map<BinaryOperationType, OperatorDefinitionNode> operators = new HashMap<>();
		for(OperatorDefinitionNode op:body.operators())
			operators.put(op.operation(), op);
		
		final EnumClassValue enumClassValue = new EnumClassValue(name, parentContext, entriesValue, functions, operators);
		
		final ConstructorValue[] constructors = new ConstructorValue[body.constructors().length];
		
		for(int i = 0; i < constructors.length; i++) {
			final Value constructorValue = body.constructors()[i].interpret(enumClassValue, interpreter);
			if(constructorValue instanceof ConstructorValue fn)
				constructors[i] = fn;
			else throw new TError(new RuntimeError("Expecting a function value for constructor " + body.constructors()[i], sequence));
		}
		
		for(Node staticNode:body.staticDeclarations())
			staticNode.interpret(enumClassValue, interpreter);
		
		for(int ordinal = 0; ordinal < entries.length; ordinal++) {
			final EnumEntry entry = entries[ordinal];
			
			final EnumEntryValue entryValue = new EnumEntryValue(enumClassValue, entry.name(), ordinal);
			entriesValue.put(entry.name(), entryValue);
			
			ConstructorValue choosenConstructor = null;
			for(ConstructorValue con:constructors) {
				if(con.numberOfArguments() == entry.args().length) {
					choosenConstructor = con;
					break;
				}
			}
			
			if(choosenConstructor == null && (constructors.length != 0 || entry.args().length > 0))
				throw new TError(new ConstructorNotFoundError(name, entry.args().length, null));
			
			final Value[] args = new Value[entry.args().length];
			for(int argIndex = 0; argIndex < args.length; argIndex++)
				args[argIndex] = entry.args()[argIndex].interpret(enumClassValue, interpreter);
			
			final NamedValue[] optionalArgs = new NamedValue[entry.optionalArgs().length];
			for(int argIndex = 0; argIndex < optionalArgs.length; argIndex++)
				optionalArgs[argIndex] = new NamedValue(entry.optionalArgs()[argIndex].name(), entry.optionalArgs()[argIndex].node().interpret(enumClassValue, interpreter));
			
			if(choosenConstructor != null)
				choosenConstructor.constructInside(enumClassValue, entryValue, interpreter, sequence, args, optionalArgs);
//				choosenConstructor.constructInside(entryValue, entryValue, interpreter, sequence, args, optionalArgs);
		}
		
		parentContext.setToThis(name, enumClassValue);
		
		return enumClassValue;
	}

}
