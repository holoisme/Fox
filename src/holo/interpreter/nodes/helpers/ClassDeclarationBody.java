package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.structures.FunctionDefinitionNode;
import holo.interpreter.nodes.structures.OperatorDefinitionNode;
import holo.interpreter.nodes.values.ConstructorExpressionNode;

public record ClassDeclarationBody(ConstructorExpressionNode[] constructors, Node[] staticDeclarations, FunctionDefinitionNode[] functions, OperatorDefinitionNode[] operators) {

	public String toString() {
		String str = "";
		
		for(Node n:staticDeclarations)
			str += "static " + n + "\n";
		
		for(Node n:constructors)
			str += "constructor" + n + "\n";
		
		for(Node n:functions)
			str += n + "\n";
		
		for(Object n:operators)
			str += n + "\n";
		
		return str;
	}

}
