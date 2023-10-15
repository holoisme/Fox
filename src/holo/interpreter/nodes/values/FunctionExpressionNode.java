package holo.interpreter.nodes.values;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.nodes.statements.ReturnNode;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.FunctionValue;
import holo.lang.lexer.Sequence;

public record FunctionExpressionNode(ObligatoryDefinitionArgument[] regularArguments, OptionalDefinitionArgument[] optionalArguments, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "("+ReflectionUtils.toString(regularArguments)+") -> " + (body instanceof ReturnNode n ? n.expression() : body);
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		return new FunctionValue(regularArguments, optionalArguments, parentContext, body);
	}
	
	public int numberOfArguments() { return regularArguments.length; }
	
}