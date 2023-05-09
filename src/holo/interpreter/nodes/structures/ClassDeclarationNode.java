package holo.interpreter.nodes.structures;

import holo.errors.AlreadyExistingVariableError;
import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.ClassDeclarationBody;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.FunctionValue;
import holo.interpreter.values.objects.ClassValue;
import holo.lang.lexer.Sequence;

public record ClassDeclarationNode(String name, ClassDeclarationBody body, Sequence sequence) implements Node {

	@Override
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		if(parentContext.contains(name))
			return onGoingRuntime.failure(new AlreadyExistingVariableError(name, sequence));
		
		FunctionValue[] constructors = new FunctionValue[body.constructors().length];
		
		ClassValue classValue = new ClassValue(name, parentContext, constructors, body.instanciateBody());
		
		for(int i = 0; i < constructors.length; i++) {
			Value constructorValue = onGoingRuntime.register(body.constructors()[i].interpret(classValue /*TODO verify*/, interpreter, onGoingRuntime), body.constructors()[i].sequence());
			if(constructorValue instanceof FunctionValue fn)
				constructors[i] = fn;
			else return onGoingRuntime.failure(new RuntimeError("Expecting a function value for constructor " + body.constructors()[i], sequence));
		}
		
		for(Node staticNode:body.staticDeclarations()) {
			onGoingRuntime.register(staticNode.interpret(classValue, interpreter, onGoingRuntime), staticNode.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		
		parentContext.setToThis(name, classValue);
		
		return onGoingRuntime.buffer(classValue);
	}

	@Override
	public String toString() {
		return "class " + name + " {\n"+body.toString().indent(4)+"}";
	}

}
