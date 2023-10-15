package holo.lang.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import holo.errors.SyntaxError;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.calls.CallNode;
import holo.interpreter.nodes.calls.NewInstanceCallNode;
import holo.interpreter.nodes.calls.SuperConstructorCall;
import holo.interpreter.nodes.calls.SuperFunctionCall;
import holo.interpreter.nodes.helpers.ClassDeclarationBody;
import holo.interpreter.nodes.helpers.ConditionnedSequence;
import holo.interpreter.nodes.helpers.EnumEntry;
import holo.interpreter.nodes.helpers.ObjectStatementSequence;
import holo.interpreter.nodes.helpers.SwitchCase;
import holo.interpreter.nodes.helpers.SwitchMultiCaseRecord;
import holo.interpreter.nodes.helpers.SwitchSingleCaseRecord;
import holo.interpreter.nodes.helpers.VarDeclaration;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.nodes.operations.BinaryBooleanOperationNode;
import holo.interpreter.nodes.operations.BinaryOperationNode;
import holo.interpreter.nodes.operations.NotNode;
import holo.interpreter.nodes.operations.NullishOperationNode;
import holo.interpreter.nodes.operations.TernaryOperationNode;
import holo.interpreter.nodes.operations.UnaryOperationNode;
import holo.interpreter.nodes.statements.AssertNode;
import holo.interpreter.nodes.statements.BreakNode;
import holo.interpreter.nodes.statements.ContinueNode;
import holo.interpreter.nodes.statements.MultiAssertNode;
import holo.interpreter.nodes.statements.ReturnNode;
import holo.interpreter.nodes.statements.SingleImportNode;
import holo.interpreter.nodes.statements.SingleLibraryImportNode;
import holo.interpreter.nodes.statements.ThrowNode;
import holo.interpreter.nodes.structures.ClassDeclarationNode;
import holo.interpreter.nodes.structures.DoWhileNode;
import holo.interpreter.nodes.structures.EnumDeclarationNode;
import holo.interpreter.nodes.structures.FileStatementsNode;
import holo.interpreter.nodes.structures.ForEachNode;
import holo.interpreter.nodes.structures.ForNode;
import holo.interpreter.nodes.structures.FunctionDefinitionNode;
import holo.interpreter.nodes.structures.IfNode;
import holo.interpreter.nodes.structures.MultiStatementsNode;
import holo.interpreter.nodes.structures.OperatorDefinitionNode;
import holo.interpreter.nodes.structures.SwitchNode;
import holo.interpreter.nodes.structures.TryCatchNode;
import holo.interpreter.nodes.structures.WhileNode;
import holo.interpreter.nodes.values.CastingNode;
import holo.interpreter.nodes.values.ConstructorExpressionNode;
import holo.interpreter.nodes.values.FunctionExpressionNode;
import holo.interpreter.nodes.values.ListNode;
import holo.interpreter.nodes.values.ObjectNode;
import holo.interpreter.nodes.values.ThisNode;
import holo.interpreter.nodes.values.TypeOfNode;
import holo.interpreter.nodes.values.ValueNode;
import holo.interpreter.nodes.var.MultiVarDeclarationNode;
import holo.interpreter.nodes.var.OptionalChainingNode;
import holo.interpreter.nodes.var.VarAccessNode;
import holo.interpreter.nodes.var.VarArrayAccessNode;
import holo.interpreter.nodes.var.VarAssignOperationNode;
import holo.interpreter.nodes.var.VarAssignQuickOperationNode;
import holo.interpreter.nodes.var.VarAssignmentNode;
import holo.interpreter.nodes.var.VarDeclarationArrayExtractNode;
import holo.interpreter.nodes.var.VarDeclarationNode;
import holo.interpreter.nodes.var.VarDeclarationPointExtractNode;
import holo.interpreter.nodes.var.VarPointAccessNode;
import holo.interpreter.types.BinaryBooleanOperationType;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.interpreter.values.primitives.StringValue;
import holo.interpreter.values.primitives.numbers.CharValue;
import holo.interpreter.values.primitives.numbers.DoubleValue;
import holo.interpreter.values.primitives.numbers.FloatValue;
import holo.interpreter.values.primitives.numbers.IntegerValue;
import holo.interpreter.values.primitives.numbers.LongValue;
import holo.lang.lexer.LexerResult;
import holo.lang.lexer.Sequence;
import holo.lang.lexer.Token;
import holo.lang.lexer.TokenType;

public class SimpleParser implements Parser {
	
	private List<Token> tokens;
	private int currentTokenIndex;
	private Token currentToken;
	
	public SimpleParser(LexerResult lexerResult) {
		if(lexerResult.hasError())
			throw new IllegalArgumentException("The lexer result has an unresolved error.");
		
		this.tokens = lexerResult.tokens();
		this.currentTokenIndex = 0;
		this.currentToken = tokens.get(0);
	}
	
	private Node leaf() {
		
		if(matches(TokenType.INTEGER)) {
			Token token = currentToken;
			advance();
			return new ValueNode(IntegerValue.get(Integer.parseInt(token.content())), token.sequence());
		}
		
		if(matches(TokenType.FLOAT)) {
			Token token = currentToken;
			advance();
			return new ValueNode(new FloatValue(Float.parseFloat(token.content())), token.sequence());
		}
		
		if(matches(TokenType.DOUBLE)) {
			Token token = currentToken;
			advance();
			return new ValueNode(new DoubleValue(Double.parseDouble(token.content())), token.sequence());
		}
		
		if(matches(TokenType.LONG)) {
			Token token = currentToken;
			advance();
			return new ValueNode(new LongValue(Long.parseLong(token.content())), token.sequence());
		}
		
		if(matches(TokenType.CHARACTER)) {
			Token token = currentToken;
			advance();
			return new ValueNode(new CharValue(token.content().charAt(0)), token.sequence());
		}
		
		if(matches(TokenType.STRING)) {
			Token token = currentToken;
			advance();
			return new ValueNode(new StringValue(token.content()), token.sequence());
		}
		
		if(matches(TokenType.IDENTIFIER)) {
			Token token = currentToken;
			advance();
			return new VarAccessNode(token.content(), token.sequence());
		}
		
		if(matches(TokenType.MINUS)) {
			Sequence sequence = sequence();
			advance();
			Node expr = access(true);
			return new UnaryOperationNode(UnaryOperationType.NEGATE, expr, sequence.join(expr.sequence()));
		}
		
		if(matches(TokenType.LEFT_PARENTHESE))
			return parenthese();
		
		if(matches(TokenType.LEFT_CURLY_BRACKET))
			return object();
		
		if(matches(TokenType.LEFT_BRACKET))
			return list();
		
		if(matches(TokenType.KEYWORD)) {
			switch(currentToken.content()) {
			case "true", "false", "null", "undefined":
				Token token = currentToken;
				advance();
				
				Value value = switch(token.content()) {
					case "true":
						yield BooleanValue.TRUE;
					case "false":
						yield BooleanValue.FALSE;
					case "null":
						yield Value.NULL;
					case "undefined":
						yield Value.UNDEFINED;
					default:
						throw new IllegalArgumentException("Unexpected value: " + token.content());
				};
				
				return new ValueNode(value, token.sequence());
			
			case "this":
				Token thisToken = currentToken;
				advance();
				return new ThisNode(thisToken.sequence());
			
			case "function":
				return function_expression();
			
			case "if":
				return if_expression();
			
			case "for":
				return for_expression();
			
			case "while":
				return while_expression();
			
			case "do":
				return do_expression();
			
			case "try":
				return try_catch_expression();
			
			case "switch":
				return switch_expression();
			
			case "new":
				return new_expression();
			
			case "class":
				return class_declaration();
			
			case "enum":
				return enum_declaration();
			
			case "throw":
				return throw_declaration();
			
			case "typeof":
				return typeof_expression();
				
			case "super":
				return super_expression();
			}
		}
		
		throw new ParseErrorHolder(new SyntaxError(currentToken));
	}
	
	private Node super_expression() {
		Sequence sequence = sequence();
		
		check("super", TokenType.KEYWORD);
		advance();
		
		check(TokenType.POINT);
		advance();
		
		Token functionName = extract(TokenType.IDENTIFIER);
		advance();
		
		CallArguments arguments = callArguments();
		
		return new SuperFunctionCall(functionName.content(), arguments.args(), arguments.optionalArgs(), sequence.join(arguments.sequence));
	}
	
	private Node typeof_expression() {
		Sequence sequence = sequence();
		
		check("typeof", TokenType.KEYWORD);
		advance();
		
		Node expression = access(true);
		return new TypeOfNode(expression, sequence.join(expression.sequence()));
	}
	
	private Node throw_declaration() {
		Sequence sequence = sequence();
		
		check("throw", TokenType.KEYWORD);
		advance();
		
		Node expression = expression();
		return new ThrowNode(expression, sequence.join(expression.sequence()));
	}
	
	private Node do_expression() {
		Sequence sequence = sequence();
		
		check("do", TokenType.KEYWORD);
		advance();
		
		Node body = null;
		if(matches(TokenType.COLON)) {
			advance();
			body = expression();
		} else if(matches(TokenType.LEFT_CURLY_BRACKET))
			body = boundedMultiStatements();
		else
			body = expression();
		
		check("while", TokenType.KEYWORD);
		advance();
		
		Node condition = expression();
		
		return new DoWhileNode(condition, body, sequence.join(condition.sequence()));
	}
	
	private Node switch_expression() {
		Sequence sequence = sequence();
		
		check("switch", TokenType.KEYWORD);
		advance();
		
		Node expression = expression();
		
		if(matches(TokenType.COLON))
			return switch_quick_expression(expression, sequence);
		
		check(TokenType.LEFT_CURLY_BRACKET);
		advance();
		
		Node catchExpression = null;
		List<SwitchCase> cases = new ArrayList<>();
		
		do {
			Sequence caseSeq = sequence();
			
			if(matches("catch", TokenType.KEYWORD)) {
				if(catchExpression != null)
					throw new ParseErrorHolder(new SyntaxError(currentToken));
				
				advance();
				
				if(matches(TokenType.COLON)) {
					advance();
					catchExpression = expression();
					
					if(matches(TokenType.DELIMITER))
						advance();
				} else if(matches(TokenType.LEFT_CURLY_BRACKET))
					catchExpression = boundedMultiStatements();	
				else throw new ParseErrorHolder(new SyntaxError(currentToken));
			}
			
			if(matches("case", TokenType.KEYWORD)) {
				advance();
				
				List<Node> equalities = new ArrayList<>();
				
				do {
					if(equalities.size() != 0)
						advance();
					
					Node expr = expression();
					
					equalities.add(expr);
				} while(matches(TokenType.COMMA));
				
				if(equalities.size() == 0)
					throw new ParseErrorHolder(new SyntaxError(currentToken, "[condition]"));
				
				Node body = null;
				if(matches(TokenType.COLON)) {
					advance();
					
					body = statement();
					
					if(matches(TokenType.DELIMITER))
						advance();
				} else if(matches(TokenType.LEFT_CURLY_BRACKET))
					body = boundedMultiStatements();
				else throw new ParseErrorHolder(new SyntaxError(currentToken));
				
				SwitchCase sCase = equalities.size() == 1 ?
									new SwitchSingleCaseRecord(equalities.get(0), body, caseSeq.join(body.sequence())):
									new SwitchMultiCaseRecord(equalities.toArray(new Node[equalities.size()]), body, caseSeq.join(body.sequence()));
				
				cases.add(sCase);
			}
		} while(matches(TokenType.KEYWORD, "case", "catch"));
		
		check(TokenType.RIGHT_CURLY_BRACKET);
		Sequence last = sequence();
		advance();
		
		return new SwitchNode(expression, cases.toArray(new SwitchCase[cases.size()]), catchExpression, sequence.join(last));
	}
	
	private Node switch_quick_expression(Node expression, Sequence sequence) {
		check(TokenType.COLON);
		advance();
		
		Node catchExpression = null;
		List<SwitchCase> cases = new ArrayList<>();
		
		do {
			if(cases.size() != 0 || catchExpression != null)
				advance();
			
			Sequence caseSeq = sequence();
			
			if(matches("catch", TokenType.KEYWORD)) {
				if(catchExpression != null)
					throw new ParseErrorHolder(new SyntaxError(currentToken));
				
				advance();
				
				check(TokenType.ARROW);
				advance();
				
				catchExpression = expression();
				
				continue;
			}
			
			List<Node> equalities = new ArrayList<>();
			
			do {
				if(equalities.size() != 0)
					advance();
				
				Node expr = expression();
				
				equalities.add(expr);
			} while(matches(TokenType.COMMA));
			
			if(equalities.size() == 0)
				throw new ParseErrorHolder(new SyntaxError(currentToken, "[condition]"));
			
			check(TokenType.ARROW);
			advance();
			
			Node body = expression();
			
			SwitchCase sCase = equalities.size() == 1 ?
								new SwitchSingleCaseRecord(equalities.get(0), body, caseSeq.join(body.sequence())):
								new SwitchMultiCaseRecord(equalities.toArray(new Node[equalities.size()]), body, caseSeq.join(body.sequence()));
			
			cases.add(sCase);
		} while(matches(TokenType.COMMA));
		
		return new SwitchNode(expression, cases.toArray(new SwitchCase[cases.size()]), catchExpression, sequence.join(cases.get(cases.size()-1).sequence()));
	}
	
	private Node new_expression() {
		Sequence sequence = sequence();
		
		check("new", TokenType.KEYWORD);
		advance();
		
		Node node = access(true);
		
		if(node instanceof CallNode callNode)
			return new NewInstanceCallNode(callNode, sequence.join(node.sequence()));
		
		throw new ParseErrorHolder(new SyntaxError(currentToken));
	}
	
	private Node var_declaration() {
		Sequence sequence = sequence();
		
		check("var", TokenType.KEYWORD);
		advance();
		
		List<VarDeclaration> declarations = new ArrayList<>();
		
		Sequence last;
		
		do {
			if(declarations.size() != 0)
				advance();
			
			Sequence singleDeclarationSequence = sequence();
			
			if(matches(TokenType.LEFT_CURLY_BRACKET)) {
				advance();
				
				List<String> varNames = new ArrayList<>();
				
				if(!matches(TokenType.RIGHT_CURLY_BRACKET)) {
					do {
						if(varNames.size() != 0)
							advance();
						
						Token name = extract(TokenType.IDENTIFIER);
						advance();
						
						varNames.add(name.content());
					} while(matches(TokenType.COMMA));
				}
				
				check(TokenType.RIGHT_CURLY_BRACKET);
				advance();
				
				check(TokenType.EQUALS);
				advance();
				
				Node expression = expression();
				
				last = expression == null ? null : expression.sequence();
				declarations.add(new VarDeclarationPointExtractNode(varNames.toArray(new String[varNames.size()]), expression, since(singleDeclarationSequence)));
				continue;
			}
			
			if(matches(TokenType.LEFT_BRACKET)) {
				advance();
				
				List<String> varNames = new ArrayList<>();
				
				if(!matches(TokenType.RIGHT_BRACKET)) {
					do {
						if(varNames.size() != 0)
							advance();
						
						Token name = extract(TokenType.IDENTIFIER);
						advance();
						
						varNames.add(name.content());
					} while(matches(TokenType.COMMA));
				}
				
				check(TokenType.RIGHT_BRACKET);
				advance();
				
				check(TokenType.EQUALS);
				advance();
				
				Node expression = expression();
				
				last = expression == null ? null : expression.sequence();
				declarations.add(new VarDeclarationArrayExtractNode(varNames.toArray(new String[varNames.size()]), expression, since(singleDeclarationSequence)));
				continue;
			}
			
			Token name = extract(TokenType.IDENTIFIER);
			advance();
			
			Node expression = null;
			if(matches(TokenType.EQUALS)) {
				advance();
				expression = expression();
			}
			
			last = expression == null ? null : expression.sequence();
			declarations.add(new VarDeclarationNode(name.content(), expression, singleDeclarationSequence.join(last)));
		} while(matches(TokenType.COMMA));
		
		if(declarations.size() == 1)
			return ((Node) declarations.get(0));
		return new MultiVarDeclarationNode(declarations.toArray(new VarDeclaration[declarations.size()]), sequence.join(last));
	}
	
	private Node assert_statement() {
		Sequence sequence = sequence();
		
		check("assert", TokenType.KEYWORD);
		advance();
		
		List<Node> conditions = new ArrayList<>();
		Sequence last;
		
		do {
			if(conditions.size() != 0)
				advance();
			
			Node expression = expression();
			
			last = expression.sequence();
			
			conditions.add(expression);
		} while(matches(TokenType.COMMA));
		
		if(conditions.size() == 1)
			return new AssertNode(conditions.get(0), sequence.join(last));
		
		return new MultiAssertNode(conditions.toArray(new Node[conditions.size()]), sequence.join(last));
	}
	
	private Node enum_declaration() {
		Sequence sequence = sequence();
		
		check("enum", TokenType.KEYWORD);
		advance();
		
		Token className = extract(TokenType.IDENTIFIER);
		advance();
		
		check(TokenType.LEFT_CURLY_BRACKET);
		advance();
		
		List<EnumEntry> entries = new ArrayList<>();
		
		do {
			if(entries.size() != 0)
				advance();
			
			Sequence singleEntrySequence = sequence();
			
			Token entryName = extract(TokenType.IDENTIFIER);
			advance();
			
			Node[] args = new Node[0];
			NamedNode[] optionalArgs = new NamedNode[0];
			if(matches(TokenType.LEFT_PARENTHESE)) {
				CallArguments arguments = callArguments();
				args = arguments.args();
				optionalArgs = arguments.optionalArgs();
			}
			
			entries.add(new EnumEntry(entryName.content(), args, optionalArgs, since(singleEntrySequence)));
		} while(matches(TokenType.COMMA));
		
		if(matches(TokenType.DELIMITER))
			advance();
		
		List<ConstructorExpressionNode> constructorDeclarations = new ArrayList<>();
		List<Node> staticDeclarations = new ArrayList<>();
		List<FunctionDefinitionNode> functionDeclatarions = new ArrayList<>();
		List<OperatorDefinitionNode> operatorDeclatarions = new ArrayList<>();
		
		while(!matches(TokenType.RIGHT_CURLY_BRACKET, TokenType.END_OF_FILE)) {
			if(matches("constructor", TokenType.KEYWORD)) {
				advance();
				DefiningArguments defArgs = definingArguments();
				
				check(TokenType.LEFT_CURLY_BRACKET);
				advance();
				
				SuperConstructorCall superCall = null;
				if(matches("super", TokenType.KEYWORD)) {
					Sequence superSequence = sequence();
					advance();
					CallArguments superCallArgs = callArguments();
					superCall = new SuperConstructorCall(superCallArgs.args, superCallArgs.optionalArgs, superSequence.join(superCallArgs.sequence()));
				}
				
				while(matches(TokenType.DELIMITER))
					advance();
				
				List<Node> nodes = new ArrayList<>();
				
				while(!matches(TokenType.RIGHT_CURLY_BRACKET, TokenType.END_OF_FILE)) {
					Node node = statement();
					
					nodes.add(node);
					while(matches(TokenType.DELIMITER))
						advance();
				}
				
				check(TokenType.RIGHT_CURLY_BRACKET);
				Sequence last = sequence();
				advance();
				
				constructorDeclarations.add(new ConstructorExpressionNode(defArgs.args, defArgs.optionalArgs, superCall, new MultiStatementsNode(nodes.toArray(new Node[nodes.size()]), last), last));
				continue;
			}
			
			if(matches("operator", TokenType.KEYWORD)) {
				Sequence opSequence = sequence();
				advance();
				Token operationToken = currentToken;
				BinaryOperationType operationType = BinaryOperationType.get(operationToken);
				advance();
				
				check(TokenType.LEFT_PARENTHESE);
				advance();
				
				Token argumentName = extract(TokenType.IDENTIFIER);
				advance();
				
				check(TokenType.RIGHT_PARENTHESE);
				advance();
				
				Node body = null;
				
				if(matches(TokenType.COLON)) {
					advance();
					
					Node expression = expression();
					
					body = new ReturnNode(expression, since(sequence));
					
					if(matches(TokenType.DELIMITER))
						advance();
				} else if(matches(TokenType.LEFT_CURLY_BRACKET))
					body = boundedMultiStatements();
				else throw new ParseErrorHolder(new SyntaxError(currentToken));
				opSequence = opSequence.join(sequence());
				
				operatorDeclatarions.add(new OperatorDefinitionNode(operationType, argumentName.content(), body, opSequence));
				continue;
			}
			
			boolean isStatic = false;
			if(matches("static", TokenType.KEYWORD)) {
				advance();
				isStatic = true;
			}
			
			if(matches(TokenType.KEYWORD)) {
				if(isStatic) {
					if(matches("var"))
						staticDeclarations.add(var_declaration());
					else if(matches("function"))
						staticDeclarations.add(function_expression());
					else if(matches("class"))
						staticDeclarations.add(class_declaration());
					else if(matches("enum"))
						staticDeclarations.add(enum_declaration());
				} else {
					if(matches("function"))
						functionDeclatarions.add((FunctionDefinitionNode) function_expression());
				}
			}
			
			while(matches(TokenType.DELIMITER))
				advance();
		}
		
		check(TokenType.RIGHT_CURLY_BRACKET);
		
		sequence = since(sequence);
		advance();

		return (
				new EnumDeclarationNode(className.content(), entries.toArray(new EnumEntry[entries.size()]),
				new ClassDeclarationBody(
					constructorDeclarations.toArray(new ConstructorExpressionNode[constructorDeclarations.size()]),
					staticDeclarations.toArray(new Node[staticDeclarations.size()]),
					functionDeclatarions.toArray(new FunctionDefinitionNode[functionDeclatarions.size()]),
					operatorDeclatarions.toArray(new OperatorDefinitionNode[operatorDeclatarions.size()])
					), since(sequence)));
	}
	
//	private Node void_class_declaration() {
//		
//		if(matches(TokenType.KEYWORD)) {
//			switch(currentToken.content()) {
//			case "var":
//				return var_declaration();
//			case "function":
//				return function_expression();
//			case "class":
//				return class_declaration();
//			case "enum":
//				return enum_declaration();
//			}
//		}
//		
//		throw new ParseErrorHolder(new SyntaxError(currentToken));
//	}
	
	// TODO
	private Node class_declaration() {
		Sequence sequence = sequence();
		
		check("class", TokenType.KEYWORD);
		advance();
		
		Token className = extract(TokenType.IDENTIFIER);
		advance();
		
		Node superClass = null;
		if(matches("extends", TokenType.KEYWORD)) {
			advance();
			superClass = access(true);
		}
		
		check(TokenType.LEFT_CURLY_BRACKET);
		advance();
		
		List<ConstructorExpressionNode> constructorDeclarations = new ArrayList<>();
		List<Node> staticDeclarations = new ArrayList<>();
		List<FunctionDefinitionNode> functionDeclatarions = new ArrayList<>();
		List<OperatorDefinitionNode> operatorDeclatarions = new ArrayList<>();
		
		while(!matches(TokenType.RIGHT_CURLY_BRACKET, TokenType.END_OF_FILE)) {
			if(matches("constructor", TokenType.KEYWORD)) {
				advance();
				DefiningArguments defArgs = definingArguments();
				
				check(TokenType.LEFT_CURLY_BRACKET);
				advance();
				
				SuperConstructorCall superCall = null;
				if(matches("super", TokenType.KEYWORD)) {
					Sequence superSequence = sequence();
					advance();
					CallArguments superCallArgs = callArguments();
					superCall = new SuperConstructorCall(superCallArgs.args, superCallArgs.optionalArgs, superSequence.join(superCallArgs.sequence()));
				}
				
				while(matches(TokenType.DELIMITER))
					advance();
				
				List<Node> nodes = new ArrayList<>();
				
				while(!matches(TokenType.RIGHT_CURLY_BRACKET, TokenType.END_OF_FILE)) {
					Node node = statement();
					
					nodes.add(node);
					while(matches(TokenType.DELIMITER))
						advance();
				}
				
				check(TokenType.RIGHT_CURLY_BRACKET);
				Sequence last = sequence();
				advance();
				
				constructorDeclarations.add(new ConstructorExpressionNode(defArgs.args, defArgs.optionalArgs, superCall, new MultiStatementsNode(nodes.toArray(new Node[nodes.size()]), last), last));
				continue;
			}
			
			if(matches("operator", TokenType.KEYWORD)) {
				Sequence opSequence = sequence();
				advance();
				Token operationToken = currentToken;
				BinaryOperationType operationType = BinaryOperationType.get(operationToken);
				advance();
				
				check(TokenType.LEFT_PARENTHESE);
				advance();
				
				Token argumentName = extract(TokenType.IDENTIFIER);
				advance();
				
				check(TokenType.RIGHT_PARENTHESE);
				advance();
				
				Node body = null;
				
				if(matches(TokenType.COLON)) {
					advance();
					
					Node expression = expression();
					
					body = new ReturnNode(expression, since(sequence));
					
					if(matches(TokenType.DELIMITER))
						advance();
				} else if(matches(TokenType.LEFT_CURLY_BRACKET))
					body = boundedMultiStatements();
				else throw new ParseErrorHolder(new SyntaxError(currentToken));
				opSequence = opSequence.join(sequence());
				
				operatorDeclatarions.add(new OperatorDefinitionNode(operationType, argumentName.content(), body, opSequence));
				continue;
			}
			
			boolean isStatic = false;
			if(matches("static", TokenType.KEYWORD)) {
				advance();
				isStatic = true;
			}
			
			if(matches(TokenType.KEYWORD)) {
				if(isStatic) {
					if(matches("var"))
						staticDeclarations.add(var_declaration());
					else if(matches("function"))
						staticDeclarations.add(function_expression());
					else if(matches("class"))
						staticDeclarations.add(class_declaration());
					else if(matches("enum"))
						staticDeclarations.add(enum_declaration());
				} else {
					if(matches("function"))
						functionDeclatarions.add((FunctionDefinitionNode) function_expression());
				}
			}
			
			while(matches(TokenType.DELIMITER))
				advance();
		}
		
		check(TokenType.RIGHT_CURLY_BRACKET);
		
		sequence = since(sequence);
		advance();
		return (
				new ClassDeclarationNode(className.content(), superClass,
				new ClassDeclarationBody(
					constructorDeclarations.toArray(new ConstructorExpressionNode[constructorDeclarations.size()]),
					staticDeclarations.toArray(new Node[staticDeclarations.size()]),
					functionDeclatarions.toArray(new FunctionDefinitionNode[functionDeclatarions.size()]),
					operatorDeclatarions.toArray(new OperatorDefinitionNode[operatorDeclatarions.size()])
					), since(sequence)));
	}
	
	private Node try_catch_expression() {
		Sequence sequence = sequence();
		
		check("try", TokenType.KEYWORD);
		advance();
		
		Node tryBody = null;
		if(matches(TokenType.COLON)) {
			advance();
			
			tryBody = statement();
		} else if(matches(TokenType.LEFT_CURLY_BRACKET))
			tryBody = boundedMultiStatements();
		else throw new ParseErrorHolder(new SyntaxError(currentToken));
		
		if(matches(TokenType.DELIMITER))
			advance();
		
		check("catch", TokenType.KEYWORD);
		advance();
		
		check(TokenType.LEFT_PARENTHESE);
		advance();
		
		Token errorVarName = extract(TokenType.IDENTIFIER);
		advance();
		
		check(TokenType.RIGHT_PARENTHESE);
		advance();
		
		Node catchBody = null;
		if(matches(TokenType.COLON)) {
			advance();
			
			catchBody = statement();
			
		} else if(matches(TokenType.LEFT_CURLY_BRACKET))
			catchBody = boundedMultiStatements();
		else throw new ParseErrorHolder(new SyntaxError(currentToken));
		
		return new TryCatchNode(tryBody, catchBody, errorVarName.content(), since(sequence));
	}
	
	private Node if_expression() {
		Sequence sequence = sequence();
		
		check("if", TokenType.KEYWORD);
		advance();
		
		List<ConditionnedSequence> conditionnedNodes = new ArrayList<>();
		Node elseBodyNode = null;
		
		do {
			Sequence conditionSequence = sequence();
			
			if(conditionnedNodes.size() != 0) {
				advance();
				
				if(matches("if", TokenType.KEYWORD))
					advance();
				else {
					if(matches(TokenType.COLON)) {
						advance();
						
						elseBodyNode = statement();
					} else if(matches(TokenType.LEFT_CURLY_BRACKET))
						elseBodyNode = boundedMultiStatements();
					else 
						elseBodyNode = statement();
					
					break;
				}
			}
			
			boolean startedWithParenthese = matches(TokenType.LEFT_PARENTHESE);
			
			Node condition = expression();
			
			Node body = null;
			if(matches(TokenType.COLON)) {
				advance();
				
				body = statement();
			} else if(matches(TokenType.LEFT_CURLY_BRACKET))
				body = boundedMultiStatements();	
			else if(startedWithParenthese)
				body = statement();	
			else throw new ParseErrorHolder(new SyntaxError(currentToken));
			
			if(matches(TokenType.DELIMITER))
				advance();
			conditionnedNodes.add(new ConditionnedSequence(condition, body, since(conditionSequence)));
		} while(matches("else", TokenType.KEYWORD));
		
		return new IfNode(conditionnedNodes.toArray(new ConditionnedSequence[conditionnedNodes.size()]), elseBodyNode, since(sequence));
	}
	
	private Node for_expression() {
		Sequence sequence = sequence();
		
		check("for", TokenType.KEYWORD);
		advance();
		
		check(TokenType.LEFT_PARENTHESE);
		advance();
		
		final int currentIndex = currentTokenIndex;
		try {
			return for_each(sequence);
		} catch(TParseFail e) {
			loadTokenAt(currentIndex);
		}
		
		Node initialization = null;
		if(!matches(TokenType.DELIMITER))
			initialization = statement();
		
		check(TokenType.DELIMITER);
		advance();
		
		Node condition = statement();
		
		check(TokenType.DELIMITER);
		advance();
		
		Node step = statement();
		
		check(TokenType.RIGHT_PARENTHESE);
		advance();
		
		Node body = null;
		if(matches(TokenType.COLON)) {
			advance();
			
			body = expression();
		} else if(matches(TokenType.LEFT_CURLY_BRACKET))
			body = boundedMultiStatements();
		else 
			body = expression();
		
		return new ForNode(initialization, condition, step, body, since(sequence));
	}
	
	private Node while_expression() {
		Sequence sequence = sequence();
		
		check("while", TokenType.KEYWORD);
		advance();
		
		boolean startedWithParenthese = matches(TokenType.LEFT_PARENTHESE);
		
		Node condition = statement();
		
		Node body = null;
		if(matches(TokenType.COLON)) {
			advance();
			
			body = expression();
		} else if(matches(TokenType.LEFT_CURLY_BRACKET))
			body = boundedMultiStatements();
		else if(startedWithParenthese)
			body = expression();
		else throw new ParseErrorHolder(new SyntaxError(currentToken));
		
		return new WhileNode(condition, body, since(sequence));
	}
	
	private Node for_each(Sequence sequence) {
		Token var = extract(false, TokenType.IDENTIFIER);
		advance();
		
		if(!matches("in", TokenType.KEYWORD))
			throw TParseFail.FAILURE;
		advance();
		
		Node list = expression();
		
		check(TokenType.RIGHT_PARENTHESE);
		advance();
		
		Node body = null;
		if(matches(TokenType.COLON)) {
			advance();
			
			body = expression();
		} else if(matches(TokenType.LEFT_CURLY_BRACKET))
			body = boundedMultiStatements();
		else body = expression();
		
		return new ForEachNode(var.content(), list, body, sequence.join(body.sequence()));
	}
	
	private Node function_expression() {
		return function_like_expression("function", true, true);
	}
	
	private Node function_like_expression(String keyword, boolean allowName, boolean autoReturn) {
		Sequence sequence = sequence();
		
		check(keyword, TokenType.KEYWORD);
		advance();
		
		Token name = null;
		if(matches(TokenType.IDENTIFIER) && allowName) {
			name = extract(TokenType.IDENTIFIER);
			advance();
		}
		
		DefiningArguments args = definingArguments();
		
		Node body = null;
		
		if(matches(TokenType.COLON)) {
			advance();
			
			Node expression = expression();
			
			body = autoReturn?new ReturnNode(expression, since(sequence)):expression;
			
			if(matches(TokenType.DELIMITER))
				advance();
		} else if(matches(TokenType.LEFT_CURLY_BRACKET))
			body = boundedMultiStatements();
		else throw new ParseErrorHolder(new SyntaxError(currentToken));
		
		FunctionExpressionNode functionExpression = new FunctionExpressionNode(args.args(), args.optionalArgs(), body, since(sequence));
		
		if(name == null || !allowName)
			return functionExpression;
		
		return new FunctionDefinitionNode(name.content(), functionExpression, sequence);
	}
	
	private static record CallArguments(Node[] args, NamedNode[] optionalArgs, Sequence sequence) {}
	private CallArguments callArguments() {
		Sequence sequence = sequence();
		List<Node> args = new ArrayList<>();
		List<NamedNode> optionalArgs = new ArrayList<>();
		
		check(TokenType.LEFT_PARENTHESE);
		advance();
		
		do {
			if(matches(TokenType.RIGHT_PARENTHESE))
				break;
			if(args.size() != 0 || optionalArgs.size() != 0)
				advance();
			
			boolean startWithIdentifier = matches(TokenType.IDENTIFIER);
			
			Node temp = expression();
			
			if(startWithIdentifier && temp instanceof VarAssignmentNode assign && assign.access() instanceof VarAccessNode access) {
				optionalArgs.add(new NamedNode(access.varName(), assign.expression()));
			} else args.add(temp);
			
		} while(matches(TokenType.COMMA));
		
		check(TokenType.RIGHT_PARENTHESE);
		sequence = sequence.join(sequence());
		advance();
		
		return new CallArguments(args.toArray(new Node[args.size()]), optionalArgs.toArray(new NamedNode[optionalArgs.size()]), sequence);
	}
	
	private static record DefiningArguments(ObligatoryDefinitionArgument[] args, OptionalDefinitionArgument[] optionalArgs) {}
	private DefiningArguments definingArguments() {
		check(TokenType.LEFT_PARENTHESE);
		advance();
		
		List<ObligatoryDefinitionArgument> regularArgs = new ArrayList<>();
		List<OptionalDefinitionArgument> optionalArgs = new ArrayList<>();
		
		do {
			if(matches(TokenType.RIGHT_PARENTHESE))
				break;
			if(regularArgs.size() != 0 || optionalArgs.size() != 0)
				advance();
			
			Token arg = extract(TokenType.IDENTIFIER);
			advance();
			
			if(matches(TokenType.EQUALS)) {
				advance();
				
				Node expression = expression();
				
				optionalArgs.add(new OptionalDefinitionArgument(arg.content(), expression, arg.sequence()));
			} else regularArgs.add(new ObligatoryDefinitionArgument(arg.content(), arg.sequence()));
			
		} while(matches(TokenType.COMMA));
		
		check(TokenType.RIGHT_PARENTHESE);
		advance();
		
		return new DefiningArguments(regularArgs.toArray(new ObligatoryDefinitionArgument[regularArgs.size()]), optionalArgs.toArray(new OptionalDefinitionArgument[optionalArgs.size()]));
	}
	
	private Node list() {
		Sequence sequence = sequence();
		
		check(TokenType.LEFT_BRACKET);
		advance();
		
		List<Node> nodes = new ArrayList<>();
		
		if(!matches(TokenType.RIGHT_BRACKET)) do {
			if(nodes.size() != 0)
				advance();
			
			Node expr = expression();
			
			nodes.add(expr);
		} while(matches(TokenType.COMMA));
		
		check(TokenType.RIGHT_BRACKET);
		sequence = sequence.join(sequence());
		advance();
		
		return new ListNode(nodes.toArray(new Node[nodes.size()]), sequence);
	}
	
	private Node object() {
		Sequence sequence = sequence();
		
		check(TokenType.LEFT_CURLY_BRACKET);
		advance();
		
		List<ObjectStatementSequence> statements = new ArrayList<>();
		do {
			if(matches(TokenType.COMMA)) {
				advance();
				
				if(matches(TokenType.RIGHT_CURLY_BRACKET))
					break;
			}
			
			Sequence objectStatementSequence = sequence();
			
			Token name = extract(TokenType.IDENTIFIER);
			advance();
			
			Node expression = null;
			
			if(matches(TokenType.COLON)) {
				advance();
				
				expression = expression();
			} else expression = new VarAccessNode(name.content(), name.sequence());
			
			statements.add(new ObjectStatementSequence(name.content(), expression, since(objectStatementSequence)));
		} while(matches(TokenType.COMMA));
		
		check(TokenType.RIGHT_CURLY_BRACKET);
		advance();
		
		return new ObjectNode(statements.toArray(new ObjectStatementSequence[statements.size()]), since(sequence));
	}
	
	private Node parenthese() {
		final int currentIndex = currentTokenIndex;
		
		try {
			return lambda();
		} catch(TParseFail e) {
			loadTokenAt(currentIndex);
		}
		
		try {
			return cast();
		} catch(TParseFail e) {
			loadTokenAt(currentIndex);
		}
		
		advance();
		
		Node expression = expression();
		
		check(TokenType.RIGHT_PARENTHESE);
		advance();
		
		return expression;
	}
	
	private Node cast() {
		Sequence sequence = sequence();
		
		check(TokenType.LEFT_PARENTHESE);
		advance();
		
		CastingType type = null;
		
		if(matches(TokenType.KEYWORD, "int", "float", "double", "char", "byte", "long", "short", "string", "boolean")) {
			Token tokenType = extract(TokenType.KEYWORD);
			advance();
			
			type = CastingType.get(tokenType.content());
		} else throw TParseFail.FAILURE;
		
		check(TokenType.RIGHT_PARENTHESE);
		advance();
		
		Node expression = access(true);
		
		return new CastingNode(type, expression, sequence.join(expression.sequence()));
	}
	
	private Node lambda() {
		Sequence sequence = sequence();
		
		check(TokenType.LEFT_PARENTHESE);
		advance();
		
		boolean committed = false;
		
		List<ObligatoryDefinitionArgument> regularArgs = new ArrayList<>();
		List<OptionalDefinitionArgument> optionalArgs = new ArrayList<>();
		
		if(!matches(TokenType.RIGHT_PARENTHESE)) do {
			if(regularArgs.size() != 0 || optionalArgs.size() != 0) {
				advance();
				committed = true;
			}
			
			Token arg = extract(committed, TokenType.IDENTIFIER);
			advance();
			
			if(matches(TokenType.EQUALS)) {
				advance();
				
				Node expression = expression();
				
				optionalArgs.add(new OptionalDefinitionArgument(arg.content(), expression, arg.sequence()));
				continue;
			}
			
			regularArgs.add(new ObligatoryDefinitionArgument(arg.content(), arg.sequence()));
		} while(matches(TokenType.COMMA));
		
		check(committed, TokenType.RIGHT_PARENTHESE);
		advance();
		
		check(committed, TokenType.ARROW);
		committed = true;
		advance();
		
		Node body = null;
		
		if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			body = boundedMultiStatements();
			
		} else {
			Sequence returnSequence = sequence();
			
			Node expression = expression();
			body = new ReturnNode(expression, since(returnSequence));
		}
		
		return new FunctionExpressionNode(regularArgs.toArray(new ObligatoryDefinitionArgument[regularArgs.size()]), optionalArgs.toArray(new OptionalDefinitionArgument[optionalArgs.size()]), body, since(sequence));
	}
	
	private Node access(boolean canFinishOnACall) {
		Node expr = leaf();
		
		boolean isLastCall = false;
		
		while(matches(TokenType.OPTIONAL_CHAINING, TokenType.POINT, TokenType.LEFT_BRACKET, TokenType.LEFT_PARENTHESE)) {
			Sequence sequence = sequence();
			
			Token token = currentToken;
			advance();
			isLastCall = false;
			
			if(token.matches(TokenType.OPTIONAL_CHAINING)) {
				Token name = extract(TokenType.IDENTIFIER);
				advance();
				
				expr = new OptionalChainingNode(expr, name.content(), name.sequence());
			}
			
			if(token.matches(TokenType.POINT)) {
				Token name = extract(TokenType.IDENTIFIER);
				advance();
				
				expr = new VarPointAccessNode(expr, name.content(), sequence);
			}
			
			if(token.matches(TokenType.LEFT_BRACKET)) {
				Node index = expression();
				
				check(TokenType.RIGHT_BRACKET);
				sequence = sequence.join(sequence());
				advance();
				
				expr = new VarArrayAccessNode(expr, index, sequence);
			}
			
			if(token.matches(TokenType.LEFT_PARENTHESE)) {
				isLastCall = true;
				
				List<Node> args = new ArrayList<>();
				List<NamedNode> optionalArgs = new ArrayList<>();
				
				do {
					if(matches(TokenType.RIGHT_PARENTHESE))
						break;
					if(args.size() != 0  || optionalArgs.size() != 0)
						advance();
					
					boolean startWithIdentifier = matches(TokenType.IDENTIFIER);
					
					Node temp = expression();
					
					if(startWithIdentifier && temp instanceof VarAssignmentNode assign && assign.access() instanceof VarAccessNode access) {
						optionalArgs.add(new NamedNode(access.varName(), assign.expression()));
					} else args.add(temp);
					
				} while(matches(TokenType.COMMA));
				
				check(TokenType.RIGHT_PARENTHESE);
				
				sequence = sequence.join(sequence());
				advance();
				
				expr = new CallNode(expr, args.toArray(new Node[args.size()]), optionalArgs.toArray(new NamedNode[optionalArgs.size()]), sequence);
			}
		}
		
		if(!canFinishOnACall && isLastCall)
			throw new ParseErrorHolder(new SyntaxError(currentToken));
		
		return expr;
	}
	
	private Node tryAssign(Node access) {
		if(matches(TokenType.EQUALS)) {
			advance();
			
			Node expr = expression();
			return new VarAssignmentNode(access, expr, access.join(expr));
		} else if(matches(TokenType.PLUS_EQUAL, TokenType.MINUS_EQUAL, TokenType.MULTIPLY_EQUAL, TokenType.DIVIDE_EQUAL, TokenType.EXPONANT_EQUAL, TokenType.MODULO_EQUAL,
				TokenType.LOGICAL_SHIFT_LEFT_EQUAL, TokenType.LOGICAL_SHIFT_RIGHT_EQUAL, TokenType.LOGICAL_STRICT_SHIFT_RIGHT_EQUAL, TokenType.LOGICAL_XOR_EQUAL, TokenType.LOGICAL_AND_EQUAL, TokenType.LOGICAL_OR_EQUAL)) {
			Token op = currentToken;
			advance();
			
			Node expr = expression();
			return new VarAssignOperationNode(access, BinaryOperationType.get(op), expr, access.join(expr));
		} else if(matches(TokenType.PLUS_PLUS, TokenType.MINUS_MINUS)) {
			Token op = currentToken;
			advance();
			
			return new VarAssignQuickOperationNode(access, QuickOperationType.get(op), access.sequence().join(op.sequence()));
		}
		
		throw TParseFail.FAILURE;
	}
	
	private Node assign() {
		Node access = access(true);
		
		if(!(access instanceof CallNode)) {
			final int currentIndex = currentTokenIndex;
			try {
				return tryAssign(access);
			} catch(TParseFail e) {
				loadTokenAt(currentIndex);
			}
		}
		
		return access;
	}
	
	private Node comp_arithmetic_exponent() {
		Node left = assign();
		
		while(matches(TokenType.EXPONANT)) {
			advance();
			
			Node right = assign();
			left = new BinaryOperationNode(BinaryOperationType.EXPONANT, left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_arithmetic_mult() {
		Node left = comp_arithmetic_exponent();
		
		while(matches(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULO)) {
			Token op = currentToken;
			advance();
			
			Node right = comp_arithmetic_exponent();
			left = new BinaryOperationNode(BinaryOperationType.get(op), left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_arithmetic_plus() {
		Node left = comp_arithmetic_mult();
		
		while(matches(TokenType.PLUS, TokenType.MINUS)) {
			Token op = currentToken;
			advance();
			
			Node right = comp_arithmetic_mult();
			left = new BinaryOperationNode(BinaryOperationType.get(op), left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_shift() {
		Node left = comp_arithmetic_plus();
		
		while(matches(TokenType.LOGICAL_SHIFT_LEFT, TokenType.LOGICAL_SHIFT_RIGHT, TokenType.LOGICAL_UNSIGNED_SHIFT_RIGHT)) {
			Token op = currentToken;
			advance();
			
			Node right = comp_arithmetic_plus();
			left = new BinaryOperationNode(BinaryOperationType.get(op), left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_logical_and() {
		Node left = comp_shift();
		
		while(matches(TokenType.LOGICAL_AND)) {
			advance();
			
			Node right = comp_shift();
			left = new BinaryOperationNode(BinaryOperationType.LOGICAL_AND, left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_logical_xor() {
		Node left = comp_logical_and();
		
		while(matches(TokenType.LOGICAL_XOR)) {
			advance();
			
			Node right = comp_logical_and();
			left = new BinaryOperationNode(BinaryOperationType.LOGICAL_XOR, left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_logical_or() {
		Node left = comp_logical_xor();
		
		while(matches(TokenType.LOGICAL_OR)) {
			advance();
			
			Node right = comp_logical_xor();
			left = new BinaryOperationNode(BinaryOperationType.LOGICAL_OR, left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_comparison() {
		Node left = comp_logical_or();
		
		while(matches(TokenType.DOUBLE_EQUALS, TokenType.NOT_EQUAL, TokenType.LESS_THAN, TokenType.LESS_OR_EQUAL, TokenType.GREATER_THAN, TokenType.GREATER_OR_EQUAL)) {
			Token op = currentToken;
			advance();
			
			Node right = comp_logical_or();
			left = new BinaryOperationNode(BinaryOperationType.get(op), left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_unary() {
		if(matches("not", TokenType.KEYWORD)) {
			Sequence sequence = sequence();
			
			advance();
			
			Node expr = comp_unary();
			return new NotNode(expr, sequence.join(expr.sequence()));
		}
		
		return comp_comparison();
	}
	
	private Node comp_binop_AND() {
		Node left = comp_unary();
		
		while(matches("and", TokenType.KEYWORD)) {
			advance();
			
			Node right = comp_unary();
			left = new BinaryBooleanOperationNode(BinaryBooleanOperationType.AND, left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_binop_OR() {
		Node left = comp_binop_AND();
		
		while(matches("or", TokenType.KEYWORD)) {
			advance();
			
			Node right = comp_binop_AND();
			left = new BinaryBooleanOperationNode(BinaryBooleanOperationType.OR, left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_binop_nullish() {
		Node left = comp_binop_OR();
		
		while(matches(TokenType.DOUBLE_QUESTION_MARK)) {
			advance();
			
			Node right = comp_binop_OR();
			left = new NullishOperationNode(left, right, left.join(right));
		}
		
		return left;
	}
	
	private Node comp_ternop() {
		Node expr = comp_binop_nullish();
		
		while(matches(TokenType.QUESTION_MARK)) {
			advance();
			
			Node ifTrue = comp_binop_nullish();
			
			check(TokenType.COLON);
			advance();
			
			Node ifFalse = comp_binop_nullish();
			expr = new TernaryOperationNode(expr, ifTrue, ifFalse, expr.join(ifFalse));
		}
		
		return expr;
	}
	
	private Node expression() {
		return comp_ternop();
	}
	
	private Node statement() {
		Sequence sequence = sequence();
		
		if(matches(TokenType.KEYWORD)) {
			switch(currentToken.content()) {
			case "var":
				return var_declaration();
			case "return":
				advance();
				
				if(!matches(TokenType.DELIMITER, TokenType.RIGHT_CURLY_BRACKET)) {
					final int currentIndex = currentTokenIndex;
					try {
						Node expression = expression();
						return new ReturnNode(expression, sequence.join(expression.sequence()));
					} catch(TParseFail e) {
						loadTokenAt(currentIndex);
					}
				}
				
				return new ReturnNode(null, sequence);
			case "break":
				advance();
				return new BreakNode(sequence);
			case "continue":
				advance();
				return new ContinueNode(sequence);
			case "library":
				advance();
				
				Token lib = extract(TokenType.STRING, TokenType.IDENTIFIER);
				advance();
				return new SingleLibraryImportNode(lib.content(), lib.type() == TokenType.IDENTIFIER, sequence.join(lib.sequence()));
			case "import":
				advance();
				
				Token token = extract(TokenType.STRING);
				advance();
				return new SingleImportNode(token.content(), sequence.join(token.sequence()));
			case "assert":
				return assert_statement();
			}
		}
		
		Node expression = expression();
		
		return expression;
	}
	
	private Node boundedMultiStatements() {
		Sequence sequence = sequence();
		
		check(TokenType.LEFT_CURLY_BRACKET);
		advance();
		
		List<Node> nodes = new ArrayList<>();
		
		while(!matches(TokenType.RIGHT_CURLY_BRACKET, TokenType.END_OF_FILE)) {
			Node node = statement();
			
			nodes.add(node);
			while(matches(TokenType.DELIMITER))
				advance();
		}
		
		check(TokenType.RIGHT_CURLY_BRACKET);
		Sequence last = sequence();
		advance();
		
		return new MultiStatementsNode(nodes.toArray(new Node[nodes.size()]), sequence.join(last));
	}
	
	public ParseResult parse() {
		Sequence sequence = sequence();
		
		List<Node> nodes = new ArrayList<>();
		try {
			while(!matches(TokenType.END_OF_FILE)) {
				Node node = statement();
				
				nodes.add(node);
				
				while(matches(TokenType.DELIMITER))
					advance();
			}
		} catch(ParseErrorHolder e) {
			return new ParseResult(null, e.error());
		}
		
		return new ParseResult(new FileStatementsNode(nodes.toArray(new Node[nodes.size()]), sequence.join(sequence())), null);
	}

	private void advance() {
		if(currentTokenIndex >= tokens.size()-1)
			return;
		this.currentTokenIndex++;
		this.currentToken = tokens.get(currentTokenIndex);
	}
	
	private boolean matches(String content) { return currentToken.matches(content); }
	private boolean matches(TokenType type) { return currentToken.matches(type); }
	private boolean matches(TokenType... types) { return currentToken.matches(types); }
	private boolean matches(String content, TokenType type) { return currentToken.matches(content, type); } 
	private boolean matches(TokenType type, String... content) { return currentToken.matches(type) && currentToken.matches(content); } 
	
	private void check(TokenType type) { check(true, type); }
	private void check(String content, TokenType type) { check(true, content, type); }
	
	private void check(boolean committed, TokenType type) {
		if(!matches(type)) {
			if(committed)
				throw new ParseErrorHolder(new SyntaxError(currentToken, type.name()));
			else throw TParseFail.FAILURE;
		}
	}
	
	private void check(boolean committed, TokenType... types) {
		if(!matches(types)) {
			if(committed)
				throw new ParseErrorHolder(new SyntaxError(currentToken, Arrays.toString(types)));
			else throw TParseFail.FAILURE;
		}
	}
	
	private void check(boolean committed, String content, TokenType type) {
		if(!matches(content, type)) {
			if(committed)
				throw new ParseErrorHolder(new SyntaxError(currentToken, type.name() + "("+content+")"));
			else throw TParseFail.FAILURE;
		}
	}
	
	private Token extract(TokenType expectedType) {
		return extract(true, expectedType);
	}
	
	private Token extract(TokenType... expectedTypes) {
		return extract(true, expectedTypes);
	}
	
	private Token extract(boolean committed, TokenType expectedType) {
		check(committed, expectedType);
		return currentToken;
	}
	
	private Token extract(boolean committed, TokenType... expectedTypes) {
		check(committed, expectedTypes);
		return currentToken;
	}
	
	private void loadTokenAt(int index) {
		currentTokenIndex = index;
		currentToken = tokens.get(index);
	}
	
	private Sequence sequence() { return currentToken.sequence(); }
	
	private Sequence since(Sequence sequence) { return sequence.join(sequence()); }
	
}
