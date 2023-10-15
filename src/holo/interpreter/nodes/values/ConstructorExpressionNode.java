package holo.interpreter.nodes.values;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.calls.SuperConstructorCall;
import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.nodes.statements.ReturnNode;
import holo.interpreter.values.Value;
import holo.interpreter.values.functions.ConstructorValue;
import holo.lang.lexer.Sequence;

public record ConstructorExpressionNode(ObligatoryDefinitionArgument[] regularArguments, OptionalDefinitionArgument[] optionalArguments, SuperConstructorCall superCall, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "("+ReflectionUtils.toString(regularArguments)+") -> " + (body instanceof ReturnNode n ? n.expression() : body);
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		return new ConstructorValue(regularArguments, optionalArguments, body, superCall);
	}
	
	public int numberOfArguments() { return regularArguments.length; }
	
}