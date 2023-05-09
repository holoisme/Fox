package holo.interpreter.nodes.var;

import java.util.Arrays;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public record MultiVarDeclarationNode(VarDeclarationNode[] declarations, Sequence sequence) implements Node {

	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		for(VarDeclarationNode declaration:declarations) {
			onGoingRuntime.register(declaration.interpret(parentContext, interpreter, onGoingRuntime), declaration.sequence());
			if(onGoingRuntime.shouldReturn()) return onGoingRuntime;
		}
		return onGoingRuntime.buffer(Value.NULL);
	}

	@Override
	public String toString() {
		return Arrays.toString(declarations);
	}

}
