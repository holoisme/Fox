package holo.interpreter.nodes.helpers;

import holo.interpreter.nodes.FunctionExpressionNode;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.structures.MultiStatementsNode;

public record ClassDeclarationBody(FunctionExpressionNode[] constructors, Node[] staticDeclarations, MultiStatementsNode instanciateBody) {

	public String toString() {
		String str = "";
		
		for(Node n:staticDeclarations)
			str += "static " + n + "\n";
		
		for(Node n:constructors)
			str += "constructor" + n + "\n";
		
		for(Node n:instanciateBody.statements())
			str += n + "\n";
		
		return str;
	}

}
