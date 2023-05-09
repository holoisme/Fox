package holo.interpreter.nodes.var;

import holo.errors.AlreadyExistingVariableError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record VarDeclarationNode(String varName, Node expression, Sequence sequence) implements Node {
	
	public String toString() {
		return "var " + varName + " = " + expression;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		if(parentContext.contains(varName))
			return onGoingRuntime.failure(new AlreadyExistingVariableError(varName, sequence));
		
		Value value = Value.NULL;
		if(expression != null) {
			value = onGoingRuntime.register(expression.interpret(parentContext, interpreter, onGoingRuntime), expression.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		
		parentContext.setToThis(varName, value);
		return onGoingRuntime.buffer(value);
	}
	
}
