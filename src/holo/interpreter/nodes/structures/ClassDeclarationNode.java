package holo.interpreter.nodes.structures;

import java.util.HashMap;
import java.util.Map;

import holo.errors.AlreadyExistingVariableError;
import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.ClassDeclarationBody;
import holo.interpreter.transcendental.TError;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.ConstructorValue;
import holo.interpreter.values.functions.FunctionValue;
import holo.interpreter.values.interfaces.IClass;
import holo.interpreter.values.objects.ClassValue;
import holo.lang.lexer.Sequence;

public record ClassDeclarationNode(String name, Node superClassAccess, ClassDeclarationBody body, Sequence sequence) implements Node {
	
	@Override
	public String toString() {
		return "class " + name + " {\n"+body.toString().indent(4)+"}";
	}
	
	@Override
	public Value interpret(Context parentContext, Interpreter interpreter) {
		if(parentContext.contains(name))
			throw new TError(new AlreadyExistingVariableError(name, sequence));
		
		final ConstructorValue[] constructors = new ConstructorValue[body.constructors().length];
		final Map<String, FunctionValue> functions = new HashMap<>();
		for(FunctionDefinitionNode fun:body.functions())
			functions.put(fun.name(), (FunctionValue) fun.functionExpression().interpret(parentContext, interpreter));
		
		final Map<BinaryOperationType, OperatorDefinitionNode> operators = new HashMap<>();
		for(OperatorDefinitionNode op:body.operators())
			operators.put(op.operation(), op);
		
		final Value superClassValue = superClassAccess != null ? superClassAccess.interpret(parentContext, interpreter) : null;
		
		if(superClassValue != null && !(superClassValue instanceof IClass))
			throw new TError(new RuntimeError("Not a class", superClassValue + " is not a class", sequence));
		
		final ClassValue classValue = new ClassValue(name, parentContext, (IClass) superClassValue, constructors, functions, operators);
		
		for(int i = 0; i < constructors.length; i++) {
			final Value constructorValue = body.constructors()[i].interpret(classValue, interpreter);
			
			if(constructorValue instanceof ConstructorValue fn)
				constructors[i] = fn;
			else throw new TError(new RuntimeError("Expecting a function value for constructor " + body.constructors()[i], sequence));
		}
		
		for(Node staticNode:body.staticDeclarations())
			staticNode.interpret(classValue, interpreter);
		
		parentContext.setToThis(name, classValue);
		
		return classValue;
	}

}
