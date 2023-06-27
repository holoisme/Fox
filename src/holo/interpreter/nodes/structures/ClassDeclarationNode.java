package holo.interpreter.nodes.structures;

import holo.errors.AlreadyExistingVariableError;
import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.ClassDeclarationBody;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.FunctionValue;
import holo.interpreter.values.objects.ClassValue;
import holo.lang.lexer.Sequence;
import holo.transcendental.TError;

public record ClassDeclarationNode(String name, ClassDeclarationBody body, Sequence sequence) implements Node {
	
	@Override
	public String toString() {
		return "class " + name + " {\n"+body.toString().indent(4)+"}";
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		if(parentContext.contains(name))
			throw new TError(new AlreadyExistingVariableError(name, sequence));
		
		FunctionValue[] constructors = new FunctionValue[body.constructors().length];
		
		ClassValue classValue = new ClassValue(name, parentContext, constructors, body.instanciateBody());
		
		for(int i = 0; i < constructors.length; i++) {
			Value constructorValue = body.constructors()[i].interpret(classValue, interpreter);
			if(constructorValue instanceof FunctionValue fn)
				constructors[i] = fn;
			else throw new TError(new RuntimeError("Expecting a function value for constructor " + body.constructors()[i], sequence));
		}
		
		for(Node staticNode:body.staticDeclarations())
			staticNode.interpret(classValue, interpreter);
		
		parentContext.setToThis(name, classValue);
		
		return classValue;
	}

}
