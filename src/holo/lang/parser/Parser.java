package holo.lang.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import holo.errors.SyntaxError;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.calls.CallNode;
import holo.interpreter.nodes.calls.NewInstanceCallNode;
import holo.interpreter.nodes.helpers.ClassDeclarationBody;
import holo.interpreter.nodes.helpers.ConditionnedSequence;
import holo.interpreter.nodes.helpers.EnumEntry;
import holo.interpreter.nodes.helpers.ObjectStatementSequence;
import holo.interpreter.nodes.helpers.SwitchCase;
import holo.interpreter.nodes.helpers.SwitchMultiCaseRecord;
import holo.interpreter.nodes.helpers.SwitchSingleCaseRecord;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.nodes.operations.BinaryBooleanOperationNode;
import holo.interpreter.nodes.operations.BinaryOperationNode;
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
import holo.interpreter.nodes.structures.ClassDeclarationNode;
import holo.interpreter.nodes.structures.DoWhileNode;
import holo.interpreter.nodes.structures.EnumDeclarationNode;
import holo.interpreter.nodes.structures.FileStatementsNode;
import holo.interpreter.nodes.structures.ForEachNode;
import holo.interpreter.nodes.structures.ForNode;
import holo.interpreter.nodes.structures.FunctionDefinitionNode;
import holo.interpreter.nodes.structures.IfNode;
import holo.interpreter.nodes.structures.MultiStatementsNode;
import holo.interpreter.nodes.structures.SwitchNode;
import holo.interpreter.nodes.structures.TryCatchNode;
import holo.interpreter.nodes.structures.WhileNode;
import holo.interpreter.nodes.values.CastingNode;
import holo.interpreter.nodes.values.CharacterNode;
import holo.interpreter.nodes.values.DefaultValueNode;
import holo.interpreter.nodes.values.FunctionExpressionNode;
import holo.interpreter.nodes.values.ListNode;
import holo.interpreter.nodes.values.ObjectNode;
import holo.interpreter.nodes.values.StringNode;
import holo.interpreter.nodes.values.ThisNode;
import holo.interpreter.nodes.values.ValueNode;
import holo.interpreter.nodes.var.MultiVarDeclarationNode;
import holo.interpreter.nodes.var.OptionalChainingNode;
import holo.interpreter.nodes.var.VarAccessNode;
import holo.interpreter.nodes.var.VarArrayAccessNode;
import holo.interpreter.nodes.var.VarAssignOperationNode;
import holo.interpreter.nodes.var.VarAssignQuickOperationNode;
import holo.interpreter.nodes.var.VarAssignmentNode;
import holo.interpreter.nodes.var.VarDeclarationNode;
import holo.interpreter.nodes.var.VarPointAccessNode;
import holo.interpreter.types.BinaryBooleanOperationType;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.types.CastingType;
import holo.interpreter.types.QuickOperationType;
import holo.interpreter.types.UnaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.BooleanValue;
import holo.interpreter.values.primitives.DoubleValue;
import holo.interpreter.values.primitives.FloatValue;
import holo.interpreter.values.primitives.IntegerValue;
import holo.lang.lexer.LexerResult;
import holo.lang.lexer.Sequence;
import holo.lang.lexer.Token;
import holo.lang.lexer.TokenType;

public class Parser {
	
	private LexerResult lexerResult;
	
	private List<Token> tokens;
	private int currentTokenIndex;
	private Token currentToken;
	
	public Parser(LexerResult lexerResult) {
		if(lexerResult.hasError())
			throw new IllegalArgumentException("The lexer result has an unresolved error.");
		
		this.lexerResult = lexerResult;
		this.tokens = lexerResult.tokens();
		this.currentTokenIndex = 0;
		this.currentToken = tokens.get(0);
	}
	
	private ParseResult leaf() {
		ParseResult pr = pr();
		
		if(matches(TokenType.INTEGER)) {
			Token token = currentToken;
			advance(pr);
			return pr.success(new ValueNode(IntegerValue.get(Integer.parseInt(token.content())), token.sequence()));
//			return pr.success(new IntegerNode(Integer.parseInt(token.content()), token.sequence()));
		}
		
		if(matches(TokenType.FLOAT)) {
			Token token = currentToken;
			advance(pr);
			return pr.success(new ValueNode(new FloatValue(Float.parseFloat(token.content())), token.sequence()));
//			return pr.success(new FloatNode(Float.parseFloat(token.content()), token.sequence()));
		}
		
		if(matches(TokenType.DOUBLE)) {
			Token token = currentToken;
			advance(pr);
			return pr.success(new ValueNode(new DoubleValue(Double.parseDouble(token.content())), token.sequence()));
//			return pr.success(new DoubleNode(Double.parseDouble(token.content()), token.sequence()));
		}
		
		if(matches(TokenType.CHARACTER)) {
			Token token = currentToken;
			advance(pr);
			return pr.success(new CharacterNode(token.content().charAt(0), token.sequence()));
		}
		
		if(matches(TokenType.STRING)) {
			Token token = currentToken;
			advance(pr);
			return pr.success(new StringNode(token.content(), token.sequence()));
		}
		
		if(matches(TokenType.IDENTIFIER)) {
			Token token = currentToken;
			advance(pr);
			return pr.success(new VarAccessNode(token.content(), token.sequence()));
		}
		
		if(matches(TokenType.MINUS)) {
			Sequence sequence = sequence();
			advance(pr);
			Node expr = pr.register(access(true));
			if(pr.shouldReturn()) return pr;
			
			return pr.success(new UnaryOperationNode(UnaryOperationType.NEGATE, expr, since(sequence)));
		}
		
		if(matches(TokenType.LEFT_PARENTHESE)) {
			Node node = pr.register(parenthese());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(node);
		}
		
		if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			Node object = pr.register(object());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(object);
		}
		
		if(matches(TokenType.LEFT_BRACKET)) {
			Node list = pr.register(list());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(list);
		}
		
		if(matches(TokenType.KEYWORD, "true", "false", "null")) {
			Token token = currentToken;
			advance(pr);
			Value val = token.matches("true") ? BooleanValue.TRUE : (token.matches("null") ? Value.NULL : BooleanValue.FALSE);
			return pr.success(new DefaultValueNode(val, token.sequence()));
		}
		
		if(matches(TokenType.KEYWORD, "this")) {
			Token token = currentToken;
			advance(pr);
			
			return pr.success(new ThisNode(token.sequence()));
		}
		
		if(matches(TokenType.KEYWORD, "function")) {
			Node function = pr.register(function_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(function);
		}
		
		if(matches(TokenType.KEYWORD, "if")) {
			Node ifExpression = pr.register(if_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(ifExpression);
		}
		
		if(matches(TokenType.KEYWORD, "for")) {
			Node forExpression = pr.register(for_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(forExpression);
		}
		
		if(matches(TokenType.KEYWORD, "while")) {
			Node whileExpression = pr.register(while_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(whileExpression);
		}
		
		if(matches(TokenType.KEYWORD, "do")) {
			Node whileExpression = pr.register(do_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(whileExpression);
		}
		
		if(matches(TokenType.KEYWORD, "try")) {
			Node tryCatchExpression = pr.register(try_catch_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(tryCatchExpression);
		}
		
		if(matches(TokenType.KEYWORD, "switch")) {
			Node switchExpression = pr.register(switch_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(switchExpression);
		}
		
		if(matches(TokenType.KEYWORD, "new")) {
			Node newExpression = pr.register(new_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(newExpression);
		}
		
		if(matches("class", TokenType.KEYWORD)) {
			Node classDeclaration = pr.register(class_declaration());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(classDeclaration);
		}
		
		if(matches("enum", TokenType.KEYWORD)) {
			Node enumDeclaration = pr.register(enum_declaration());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(enumDeclaration);
		}
		
//		if(matches("assert", TokenType.KEYWORD)) {
//			Node assertStatement = pr.register(assert_statement());
//			if(pr.shouldReturn()) return pr;
//			
//			return pr.success(assertStatement);
//		}
		
		return pr.failure(new SyntaxError(currentToken));
	}
	
	private ParseResult do_expression() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, "do", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node body = null;
		if(matches(TokenType.COLON)) {
			advance(pr);
			
			body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
		} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			body = pr.register(boundedMultiStatements());
			if(pr.shouldReturn()) return pr;
		} else {
			body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
		}
		
		check(pr, "while", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node condition = pr.register(expression());
		if(pr.shouldReturn()) return pr;
		
		return pr.success(new DoWhileNode(condition, body, since(sequence)));
	}
	
	private ParseResult switch_expression() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, "switch", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node expression = pr.register(expression());
		if(pr.shouldReturn()) return pr;
		
		if(matches(TokenType.COLON))
			return switch_quick_expression(expression, pr, sequence);
		
		check(pr, TokenType.LEFT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node catchExpression = null;
		List<SwitchCase> cases = new ArrayList<>();
		
		do {
			Sequence caseSeq = sequence();
			
			if(matches(TokenType.KEYWORD, "catch")) {
				if(catchExpression != null)
					return pr.failure(new SyntaxError(currentToken));
				
				advance(pr);
				
				if(matches(TokenType.COLON)) {
					advance(pr);
					catchExpression = pr.register(expression());
					if(pr.shouldReturn()) return pr;
					
					if(matches(TokenType.DELIMITER))
						advance(pr);
				} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
					catchExpression = pr.register(boundedMultiStatements());
					if(pr.shouldReturn()) return pr;
				} else return pr.failure(new SyntaxError(currentToken));
			}
			
			if(matches(TokenType.KEYWORD, "case")) {
				advance(pr);
				
				List<Node> equalities = new ArrayList<>();
				
				do {
					if(equalities.size() != 0)
						advance(pr);
					
					Node expr = pr.register(expression());
					if(pr.shouldReturn()) return pr;
					
					equalities.add(expr);
				} while(matches(TokenType.COMMA));
				
				if(equalities.size() == 0)
					return pr.failure(new SyntaxError(currentToken, "[condition]"));
				
				Node body = null;
				if(matches(TokenType.COLON)) {
					advance(pr);
					
					body = pr.register(statement());
					if(pr.shouldReturn()) return pr;
					
					if(matches(TokenType.DELIMITER))
						advance(pr);
				} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
					body = pr.register(boundedMultiStatements());
					if(pr.shouldReturn()) return pr;
				} else return pr.failure(new SyntaxError(currentToken));
				
				SwitchCase sCase = equalities.size() == 1 ?
									new SwitchSingleCaseRecord(equalities.get(0), body, since(caseSeq)):
									new SwitchMultiCaseRecord(equalities.toArray(new Node[equalities.size()]), body, since(caseSeq));
				
				cases.add(sCase);
			}
		} while(matches(TokenType.KEYWORD, "case", "catch"));
		
		check(pr, TokenType.RIGHT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		return pr.success(new SwitchNode(expression, cases.toArray(new SwitchCase[cases.size()]), catchExpression, since(sequence)));
	}
	
	private ParseResult switch_quick_expression(Node expression, ParseResult pr, Sequence sequence) {
		check(pr, TokenType.COLON);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node catchExpression = null;
		List<SwitchCase> cases = new ArrayList<>();
		
		do {
			if(cases.size() != 0)
				advance(pr);
			
			Sequence caseSeq = sequence();
			
			if(matches(TokenType.KEYWORD, "catch")) {
				if(catchExpression != null)
					return pr.failure(new SyntaxError(currentToken));
				
				advance(pr);
				
				check(pr, TokenType.ARROW);
				if(pr.shouldReturn()) return pr;
				advance(pr);
				
				catchExpression = pr.register(expression());
				if(pr.shouldReturn()) return pr;
				
				continue;
			}
			
			List<Node> equalities = new ArrayList<>();
			
			do {
				if(equalities.size() != 0)
					advance(pr);
				
				Node expr = pr.register(expression());
				if(pr.shouldReturn()) return pr;
				
				equalities.add(expr);
			} while(matches(TokenType.COMMA));
			
			if(equalities.size() == 0)
				return pr.failure(new SyntaxError(currentToken, "[condition]"));
			
			check(pr, TokenType.ARROW);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			Node body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
			
			SwitchCase sCase = equalities.size() == 1 ?
								new SwitchSingleCaseRecord(equalities.get(0), body, since(caseSeq)):
								new SwitchMultiCaseRecord(equalities.toArray(new Node[equalities.size()]), body, since(caseSeq));
			
			cases.add(sCase);
		} while(matches(TokenType.COMMA));
		
		return pr.success(new SwitchNode(expression, cases.toArray(new SwitchCase[cases.size()]), catchExpression, since(sequence)));
	}
	
	private ParseResult new_expression() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, "new", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node node = pr.register(access(true));
		if(pr.shouldReturn()) return pr;
		
		if(node instanceof CallNode callNode)
			return pr.success(new NewInstanceCallNode(callNode, since(sequence)));
		
		return pr.failure(new SyntaxError(currentToken));
	}
	
	private ParseResult var_declaration() {
		ParseResult pr = new ParseResult();
		Sequence sequence = sequence();
		
		check(pr, "var", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<VarDeclarationNode> declarations = new ArrayList<>();
		
		do {
			if(declarations.size() != 0)
				advance(pr);
			
			Sequence singleDeclarationSequence = sequence();
			
			Token name = extract(pr, TokenType.IDENTIFIER);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			Node expression = null;
			if(matches(TokenType.EQUALS)) {
				advance(pr);
				expression = pr.register(expression());
				if(pr.shouldReturn()) return pr;
			}
			
			declarations.add(new VarDeclarationNode(name.content(), expression, since(singleDeclarationSequence)));
		} while(matches(TokenType.COMMA));
		
		if(declarations.size() == 1)
			return pr.success(declarations.get(0));
		return pr.success(new MultiVarDeclarationNode(declarations.toArray(new VarDeclarationNode[declarations.size()]), since(sequence)));
	}
	
	private ParseResult assert_statement() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, "assert", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<Node> conditions = new ArrayList<>();
		
		do {
			if(conditions.size() != 0)
				advance(pr);
			
			Node expression = pr.register(expression());
			if(pr.shouldReturn()) return pr;
			
			conditions.add(expression);
		} while(matches(TokenType.COMMA));
		
		if(conditions.size() == 1)
			return pr.success(new AssertNode(conditions.get(0), since(sequence)));
		
		return pr.success(new MultiAssertNode(conditions.toArray(new Node[conditions.size()]), since(sequence)));
	}
	
	private ParseResult void_class_declaration() {
		ParseResult pr = new ParseResult();
		
		if(matches("var", TokenType.KEYWORD)) {
			Node varDeclaration = pr.register(var_declaration());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(varDeclaration);
		} else if(matches("function", TokenType.KEYWORD)) {
			Node function = pr.register(function_expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(function);
		} else if(matches("class", TokenType.KEYWORD)) {
			Node classDeclaration = pr.register(class_declaration());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(classDeclaration);
		} else if(matches("enum", TokenType.KEYWORD)) {
			Node enumDeclaration = pr.register(enum_declaration());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(enumDeclaration);
		}
		
		return pr.failure(new SyntaxError(currentToken));
	}
	
	private ParseResult enum_declaration() {
		ParseResult pr = new ParseResult();
		Sequence sequence = sequence();
		
		check(pr, "enum", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Token className = extract(pr, TokenType.IDENTIFIER);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		check(pr, TokenType.LEFT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<EnumEntry> entries = new ArrayList<>();
		
		do {
			if(entries.size() != 0)
				advance(pr);
			
			Sequence singleEntrySequence = sequence();
			
			Token entryName = extract(pr, TokenType.IDENTIFIER);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			List<Node> args = new ArrayList<>();
			if(matches(TokenType.LEFT_PARENTHESE)) {
				advance(pr);
				
				do {
					if(matches(TokenType.RIGHT_PARENTHESE))
						break;
					if(args.size() != 0)
						advance(pr);
					
					Node temp = pr.register(expression());
					if(pr.shouldReturn()) return pr;
					
					args.add(temp);
				} while(matches(TokenType.COMMA));
				
				check(pr, TokenType.RIGHT_PARENTHESE);
				if(pr.shouldReturn()) return pr;
				advance(pr);
			}
			
			entries.add(new EnumEntry(entryName.content(), args.toArray(new Node[args.size()]), since(singleEntrySequence)));
		} while(matches(TokenType.COMMA));
		
		if(matches(TokenType.DELIMITER))
			advance(pr);
		
		List<FunctionExpressionNode> constructorDeclarations = new ArrayList<>();
		List<Node> staticDeclarations = new ArrayList<>();
		List<Node> regularDeclarations = new ArrayList<>();
		
		while(!matches(TokenType.RIGHT_CURLY_BRACKET, TokenType.END_OF_FILE)) {
			if(matches("constructor", TokenType.KEYWORD)) {
				Node constructor = pr.register(function_like_expression("constructor", false, false));
				if(pr.shouldReturn()) return pr;
				
				if(!(constructor instanceof FunctionExpressionNode)) {
					System.err.println("Expected a function expression node");
					return pr.failure(new SyntaxError(currentToken));
				}
				
				constructorDeclarations.add((FunctionExpressionNode) constructor);
				continue;
			}
			
			boolean isStatic = false;
			if(matches("static", TokenType.KEYWORD)) {
				advance(pr);
				isStatic = true;
			}
			
			Node node = pr.register(void_class_declaration());
			if(pr.shouldReturn()) return pr;
			
			if(isStatic) staticDeclarations.add(node);
			else regularDeclarations.add(node);
			
			while(matches(TokenType.DELIMITER))
				advance(pr);
		}
		
		check(pr, TokenType.RIGHT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		sequence = since(sequence);
		advance(pr);
		
		return pr.success(
				new EnumDeclarationNode(className.content(), entries.toArray(new EnumEntry[entries.size()]),
				new ClassDeclarationBody(
					constructorDeclarations.toArray(new FunctionExpressionNode[constructorDeclarations.size()]),
					staticDeclarations.toArray(new Node[staticDeclarations.size()]),
					new MultiStatementsNode(regularDeclarations.toArray(new Node[regularDeclarations.size()]), since(sequence))
					), since(sequence)));
	}
	
	private ParseResult class_declaration() {
		ParseResult pr = new ParseResult();
		Sequence sequence = sequence();
		
		check(pr, "class", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Token className = extract(pr, TokenType.IDENTIFIER);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		check(pr, TokenType.LEFT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<FunctionExpressionNode> constructorDeclarations = new ArrayList<>();
		List<Node> staticDeclarations = new ArrayList<>();
		List<Node> regularDeclarations = new ArrayList<>();
		
		while(!matches(TokenType.RIGHT_CURLY_BRACKET, TokenType.END_OF_FILE)) {
			if(matches("constructor", TokenType.KEYWORD)) {
				Node constructor = pr.register(function_like_expression("constructor", false, false));
				if(pr.shouldReturn()) return pr;
				
				if(!(constructor instanceof FunctionExpressionNode)) {
					System.err.println("Expected a function expression node");
					return pr.failure(new SyntaxError(currentToken));
				}
				
				constructorDeclarations.add((FunctionExpressionNode) constructor);
				continue;
			}
			
			boolean isStatic = false;
			if(matches("static", TokenType.KEYWORD)) {
				advance(pr);
				isStatic = true;
			}
			
			Node node = pr.register(void_class_declaration());
			if(pr.shouldReturn()) return pr;
			
			if(isStatic) staticDeclarations.add(node);
			else regularDeclarations.add(node);
			
			while(matches(TokenType.DELIMITER))
				advance(pr);
		}
		
		check(pr, TokenType.RIGHT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		sequence = since(sequence);
		advance(pr);
		
		return pr.success(
				new ClassDeclarationNode(className.content(),
				new ClassDeclarationBody(
					constructorDeclarations.toArray(new FunctionExpressionNode[constructorDeclarations.size()]),
					staticDeclarations.toArray(new Node[staticDeclarations.size()]),
					new MultiStatementsNode(regularDeclarations.toArray(new Node[regularDeclarations.size()]), since(sequence))
					), since(sequence)));
	}
	
	private ParseResult try_catch_expression() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, "try", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node tryBody = null;
		if(matches(TokenType.COLON)) {
			advance(pr);
			
			tryBody = pr.register(statement());
			if(pr.shouldReturn()) return pr;
		} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			tryBody = pr.register(boundedMultiStatements());
			if(pr.shouldReturn()) return pr;
		} else return pr.failure(new SyntaxError(currentToken));
		
		if(matches(TokenType.DELIMITER))
			advance(pr);
		
		check(pr, "catch", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		check(pr, TokenType.LEFT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Token errorVarName = extract(pr, TokenType.IDENTIFIER);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		check(pr, TokenType.RIGHT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node catchBody = null;
		if(matches(TokenType.COLON)) {
			advance(pr);
			
			catchBody = pr.register(statement());
			if(pr.shouldReturn()) return pr;
		} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			catchBody = pr.register(boundedMultiStatements());
			if(pr.shouldReturn()) return pr;
		} else return pr.failure(new SyntaxError(currentToken));
		
		return pr.success(new TryCatchNode(tryBody, catchBody, errorVarName.content(), since(sequence)));
	}
	
	private ParseResult if_expression() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, "if", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<ConditionnedSequence> conditionnedNodes = new ArrayList<>();
		Node elseBodyNode = null;
		
		do {
			Sequence conditionSequence = sequence();
			
			if(conditionnedNodes.size() != 0) {
				advance(pr);
				
				if(matches("if", TokenType.KEYWORD))
					advance(pr);
				else {
					
//					ignore(pr, TokenType.COLON);
					
					if(matches(TokenType.COLON)) {
						advance(pr);
						
						elseBodyNode = pr.register(statement());
						if(pr.shouldReturn()) return pr;
					} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
						elseBodyNode = pr.register(boundedMultiStatements());
						if(pr.shouldReturn()) return pr;
					} else {
						elseBodyNode = pr.register(statement());
						if(pr.shouldReturn()) return pr;
					}
					
					//else return pr.failure(new SyntaxError(currentToken));
					
					break;
				}
			}
			
			boolean startedWithParenthese = matches(TokenType.LEFT_PARENTHESE);
			
			Node condition = pr.register(expression());
			if(pr.shouldReturn()) return pr;
			
			Node body = null;
			if(matches(TokenType.COLON)) {
				advance(pr);
				
				body = pr.register(statement());
				if(pr.shouldReturn()) return pr;
			} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
				body = pr.register(boundedMultiStatements());
				if(pr.shouldReturn()) return pr;
			} else if(startedWithParenthese) {
				body = pr.register(statement());
				if(pr.shouldReturn()) return pr;
			} else return pr.failure(new SyntaxError(currentToken));
			
			if(matches(TokenType.DELIMITER))
				advance(pr);
			conditionnedNodes.add(new ConditionnedSequence(condition, body, since(conditionSequence)));
		} while(matches("else", TokenType.KEYWORD));
		
		return pr.success(new IfNode(conditionnedNodes.toArray(new ConditionnedSequence[conditionnedNodes.size()]), elseBodyNode, since(sequence)));
	}
	
	private ParseResult for_expression() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, "for", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		check(pr, TokenType.LEFT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node possibleForEach = pr.tryRegister(for_each());
		if(pr.shouldHandleTried()) return pr.handleTried(possibleForEach);
		reverse(pr);
		
		Node initialization = null;
		if(!matches(TokenType.DELIMITER)) {
			initialization = pr.register(statement());
			if(pr.shouldReturn()) return pr;
		}
		
		check(pr, TokenType.DELIMITER);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node condition = pr.register(statement());
		if(pr.shouldReturn()) return pr;
		
		check(pr, TokenType.DELIMITER);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node step = pr.register(statement());
		if(pr.shouldReturn()) return pr;
		
		check(pr, TokenType.RIGHT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node body = null;
		if(matches(TokenType.COLON)) {
			advance(pr);
			
			body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
		} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			body = pr.register(boundedMultiStatements());
			if(pr.shouldReturn()) return pr;
		} else {
			body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
		}
		
		return pr.success(new ForNode(initialization, condition, step, body, since(sequence)));
	}
	
	private ParseResult while_expression() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, "while", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		boolean startedWithParenthese = matches(TokenType.LEFT_PARENTHESE);
		
		Node condition = pr.register(statement());
		if(pr.shouldReturn()) return pr;
		
		Node body = null;
		if(matches(TokenType.COLON)) {
			advance(pr);
			
			body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
		} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			body = pr.register(boundedMultiStatements());
			if(pr.shouldReturn()) return pr;
		} else if(startedWithParenthese) {
			body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
		} else return pr.failure(new SyntaxError(currentToken));
		
		return pr.success(new WhileNode(condition, body, since(sequence)));
	}
	
	private ParseResult for_each() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Token var = extract(pr, TokenType.IDENTIFIER);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		check(pr, "in", TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		pr.commit();
		
		Node list = pr.register(expression());
		if(pr.shouldReturn()) return pr;
		
		check(pr, TokenType.RIGHT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node body = null;
		if(matches(TokenType.COLON)) {
			advance(pr);
			
			body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
		} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			body = pr.register(boundedMultiStatements());
			if(pr.shouldReturn()) return pr;
		} else {
			body = pr.register(expression());
			if(pr.shouldReturn()) return pr;
		}
		
		return pr.success(new ForEachNode(var.content(), list, body, since(sequence)));
	}
	
	private ParseResult function_expression() {
		return function_like_expression("function", true, true);
	}
	
	private ParseResult function_like_expression(String keyword, boolean allowName, boolean autoReturn) {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, keyword, TokenType.KEYWORD);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Token name = null;
		if(matches(TokenType.IDENTIFIER) && allowName) {
			name = extract(pr, TokenType.IDENTIFIER);
			advance(pr);
		}
		
		check(pr, TokenType.LEFT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<ObligatoryDefinitionArgument> regularArgs = new ArrayList<>();
		List<OptionalDefinitionArgument> optionalArgs = new ArrayList<>();
		
		if(!matches(TokenType.RIGHT_PARENTHESE)) do {
			if(regularArgs.size() != 0 || optionalArgs.size() != 0)
				advance(pr);
			
			Token arg = extract(pr, TokenType.IDENTIFIER);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			if(matches(TokenType.EQUALS)) {
				advance(pr);
				
				Node expression = pr.register(expression());
				if(pr.shouldReturn()) return pr;
				
				optionalArgs.add(new OptionalDefinitionArgument(arg.content(), expression, arg.sequence()));
			} else regularArgs.add(new ObligatoryDefinitionArgument(arg.content(), arg.sequence()));
			
		} while(matches(TokenType.COMMA));
		
		check(pr, TokenType.RIGHT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node body = null;
		
		if(matches(TokenType.COLON)) {
			advance(pr);
			
			Node expression = pr.register(expression());
			if(pr.shouldReturn()) return pr;
			
			body = autoReturn?new ReturnNode(expression, since(sequence)):expression;
			
			if(matches(TokenType.DELIMITER))
				advance(pr);
		} else if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			body = pr.register(boundedMultiStatements());
			if(pr.shouldReturn()) return pr;
		} else pr.failure(new SyntaxError(currentToken));
		
		FunctionExpressionNode functionExpression = new FunctionExpressionNode(regularArgs.toArray(new ObligatoryDefinitionArgument[regularArgs.size()]), optionalArgs.toArray(new OptionalDefinitionArgument[optionalArgs.size()]), body, since(sequence));
		
		if(name == null || !allowName)
			return pr.success(functionExpression);
		
		return pr.success(new FunctionDefinitionNode(name.content(), functionExpression, sequence));
//		return pr.success(new FunctionDefinitionNode(name==null?null:name.content(), args.toArray(new DefinitionArgumentSequence[args.size()]), body, since(sequence)));
	}
	
	private ParseResult list() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, TokenType.LEFT_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<Node> nodes = new ArrayList<>();
		
		if(!matches(TokenType.RIGHT_BRACKET)) do {
			if(nodes.size() != 0)
				advance(pr);
			
			Node expr = pr.register(expression());
			if(pr.shouldReturn()) return pr;
			
			nodes.add(expr);
		} while(matches(TokenType.COMMA));
		
		check(pr, TokenType.RIGHT_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		return pr.success(new ListNode(nodes.toArray(new Node[nodes.size()]), since(sequence)));
	}
	
	private ParseResult object() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, TokenType.LEFT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<ObjectStatementSequence> statements = new ArrayList<>();
		do {
			if(matches(TokenType.COMMA)) {
				advance(pr);
				
				if(matches(TokenType.RIGHT_CURLY_BRACKET))
					break;
			}
//			if(statements.size() != 0)
//				advance(pr);
			
			Sequence objectStatementSequence = sequence();
			
			Token name = extract(pr, TokenType.IDENTIFIER);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			Node expression = null;
			
			if(matches(TokenType.COLON)) {
				advance(pr);
				
				expression = pr.register(expression());
				if(pr.shouldReturn()) return pr;
			} else expression = new VarAccessNode(name.content(), name.sequence());
			
			statements.add(new ObjectStatementSequence(name.content(), expression, since(objectStatementSequence)));
		} while(matches(TokenType.COMMA));
		
		check(pr, TokenType.RIGHT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		return pr.success(new ObjectNode(statements.toArray(new ObjectStatementSequence[statements.size()]), since(sequence)));
	}
	
	private ParseResult parenthese() {
		ParseResult pr = pr();
		
		Node possibleLambda = pr.tryRegister(lambda());
		if(pr.shouldHandleTried()) return pr.handleTried(possibleLambda);
		reverse(pr);
		
		Node possibleCast = pr.tryRegister(cast());
		if(pr.shouldHandleTried()) return pr.handleTried(possibleCast);
		reverse(pr);
		
		advance(pr);
		
		Node expression = pr.register(expression());
		if(pr.shouldReturn()) return pr;
		check(pr, TokenType.RIGHT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		return pr.success(expression);
	}
	
	private ParseResult cast() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, TokenType.LEFT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		CastingType type = null;
		
		if(matches(TokenType.KEYWORD, "int", "float", "double", "char", "string", "boolean")) {
			pr.commit();
			
			Token tokenType = extract(pr, TokenType.KEYWORD);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			type = CastingType.get(tokenType.content());
		} else pr.failure(new SyntaxError(currentToken));
		
		check(pr, TokenType.RIGHT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node expression = pr.register(access(true));
		if(pr.shouldReturn()) return pr;
		
		return pr.success(new CastingNode(type, expression, since(sequence)));
	}
	
	private ParseResult lambda() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, TokenType.LEFT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<ObligatoryDefinitionArgument> regularArgs = new ArrayList<>();
		List<OptionalDefinitionArgument> optionalArgs = new ArrayList<>();
		
		if(!matches(TokenType.RIGHT_PARENTHESE)) do {
			if(regularArgs.size() != 0 || optionalArgs.size() != 0)
				advance(pr);
			
			Token arg = extract(pr, TokenType.IDENTIFIER);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			if(matches(TokenType.EQUALS)) {
				advance(pr);
				
				Node expression = pr.register(expression());
				if(pr.shouldReturn()) return pr;
				
				optionalArgs.add(new OptionalDefinitionArgument(arg.content(), expression, arg.sequence()));
				continue;
			}
			
			regularArgs.add(new ObligatoryDefinitionArgument(arg.content(), arg.sequence()));
		} while(matches(TokenType.COMMA));
		
		check(pr, TokenType.RIGHT_PARENTHESE);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		check(pr, TokenType.ARROW);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		Node body = null;
		
		if(matches(TokenType.LEFT_CURLY_BRACKET)) {
			body = pr.register(boundedMultiStatements());
			if(pr.shouldReturn()) return pr;
		} else {
			Sequence returnSequence = sequence();
			
			Node expression = pr.register(expression());
			if(pr.shouldReturn()) return pr;
			
			body = new ReturnNode(expression, since(returnSequence));
		}
		
		return pr.success(new FunctionExpressionNode(regularArgs.toArray(new ObligatoryDefinitionArgument[regularArgs.size()]), optionalArgs.toArray(new OptionalDefinitionArgument[optionalArgs.size()]), body, since(sequence)));
//		return pr.success(new FunctionExpressionNode(args.toArray(new DefinitionArgument[args.size()]), body, since(sequence)));
	}
	
	private ParseResult access(boolean canFinishOnACall) {
		ParseResult pr = pr();
		
		Node expr = pr.register(leaf());
		if(pr.shouldReturn()) return pr;
		
		boolean isLastCall = false;
		
		while(matches(TokenType.OPTIONAL_CHAINING, TokenType.POINT, TokenType.LEFT_BRACKET, TokenType.LEFT_PARENTHESE)) {
			Sequence sequence = sequence();
			
			Token token = currentToken;
			advance(pr);
			isLastCall = false;
			
			if(token.matches(TokenType.OPTIONAL_CHAINING)) {
				Token name = extract(pr, TokenType.IDENTIFIER);
				if(pr.shouldReturn()) return pr;
				advance(pr);
				
				expr = new OptionalChainingNode(expr, name.content(), since(sequence));
			}
			
			if(token.matches(TokenType.POINT)) {
				Token name = extract(pr, TokenType.IDENTIFIER);
				if(pr.shouldReturn()) return pr;
				advance(pr);
				
				expr = new VarPointAccessNode(expr, name.content(), since(sequence));
			}
			
			if(token.matches(TokenType.LEFT_BRACKET)) {
				Node index = pr.register(expression());
				if(pr.shouldReturn()) return pr;
				
				check(pr, TokenType.RIGHT_BRACKET);
				if(pr.shouldReturn()) return pr;
				advance(pr);
				
				expr = new VarArrayAccessNode(expr, index, since(sequence));
			}
			
			if(token.matches(TokenType.LEFT_PARENTHESE)) {
				isLastCall = true;
				
				List<Node> args = new ArrayList<>();
				List<NamedNode> optionalArgs = new ArrayList<>();
				
				do {
					if(matches(TokenType.RIGHT_PARENTHESE))
						break;
					if(args.size() != 0  || optionalArgs.size() != 0)
						advance(pr);
					
//					if(matches(TokenType.IDENTIFIER)) {
//						ParseResult optPr = new ParseResult();
//						String name = currentToken.content();
//						
//						advance(optPr);
//						if(matches(TokenType.EQUALS)) {
//							optPr.commit();
//							advance(optPr);
//							
//							Node temp = optPr.register(expression());
//							if(optPr.shouldReturn()) return optPr;
//							
//							optionalArgs.add(new NamedNode(name, temp));
//							pr.register(optPr);
//							continue;
//						} else reverse(optPr);
//					}
					
					boolean startWithIdentifier = matches(TokenType.IDENTIFIER);
					
					Node temp = pr.register(expression());
					if(pr.shouldReturn()) return pr;
					
					if(startWithIdentifier && temp instanceof VarAssignmentNode assign && assign.access() instanceof VarAccessNode access) {
						optionalArgs.add(new NamedNode(access.varName(), assign.expression()));
					} else args.add(temp);
					
				} while(matches(TokenType.COMMA));
				
				check(pr, TokenType.RIGHT_PARENTHESE);
				if(pr.shouldReturn()) return pr;
				advance(pr);
				
				expr = new CallNode(expr, args.toArray(new Node[args.size()]), optionalArgs.toArray(new NamedNode[optionalArgs.size()]), since(sequence));
			}
		}
		
		if(!canFinishOnACall && isLastCall)
			return pr.failure(new SyntaxError(currentToken));
		
		return pr.success(expr);
	}
	
	private ParseResult tryAssign() {
		ParseResult pr = pr();
		
		Node access = pr.register(access(false));
		if(pr.shouldReturn()) return pr;
		
		Sequence sequence = sequence();
		
		if(matches(TokenType.EQUALS)) {
			pr.commit();
			advance(pr);
			
			Node expr = pr.register(expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(new VarAssignmentNode(access, expr, since(sequence)));
		} else if(matches(TokenType.PLUS_EQUAL, TokenType.MINUS_EQUAL, TokenType.MULTIPLY_EQUAL, TokenType.DIVIDE_EQUAL, TokenType.EXPONANT_EQUAL, TokenType.MODULO_EQUAL)) {
			pr.commit();
			Token op = currentToken;
			advance(pr);
			
			Node expr = pr.register(expression());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(new VarAssignOperationNode(access, BinaryOperationType.get(op), expr, since(sequence)));
		} else if(matches(TokenType.PLUS_PLUS, TokenType.MINUS_MINUS)) {
			pr.commit();
			Token op = currentToken;
			advance(pr);
			
			return pr.success(new VarAssignQuickOperationNode(access, QuickOperationType.get(op), since(sequence)));
		}
		
		return pr.failure(new SyntaxError(currentToken));
	}
	
	private ParseResult assign() {
		ParseResult pr = pr();
		
		Node assign = pr.tryRegister(tryAssign());
//		if(!pr.shouldReverse()) return pr.success(assign);
		if(pr.shouldHandleTried()) return pr.handleTried(assign);
		reverse(pr);
		
		Node access = pr.register(access(true));
		if(pr.shouldReturn()) return pr;
		
		return pr.success(access);
	}
	
	private ParseResult comp_arithmetic_exponent() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Node left = pr.register(assign());
		if(pr.shouldReturn()) return pr;
		
		while(matches(TokenType.EXPONANT)) {
			advance(pr);
			
			Node right = pr.register(assign());
			if(pr.shouldReturn()) return pr;
			
			left = new BinaryOperationNode(BinaryOperationType.EXPONANT, left, right, since(sequence));
		}
		
		return pr.success(left);
	}
	
	private ParseResult comp_arithmetic_mult() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Node left = pr.register(comp_arithmetic_exponent());
		if(pr.shouldReturn()) return pr;
		
		while(matches(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULO)) {
			Token op = currentToken;
			advance(pr);
			
			Node right = pr.register(comp_arithmetic_exponent());
			if(pr.shouldReturn()) return pr;
			
			left = new BinaryOperationNode(BinaryOperationType.get(op), left, right, since(sequence));
		}
		
		return pr.success(left);
	}
	
	private ParseResult comp_arithmetic_plus() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Node left = pr.register(comp_arithmetic_mult());
		if(pr.shouldReturn()) return pr;
		
		while(matches(TokenType.PLUS, TokenType.MINUS)) {
			Token op = currentToken;
			advance(pr);
			
			Node right = pr.register(comp_arithmetic_mult());
			if(pr.shouldReturn()) return pr;
			
			left = new BinaryOperationNode(BinaryOperationType.get(op), left, right, since(sequence));
		}
		
		return pr.success(left);
	}
	
	private ParseResult comp_comparison() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Node left = pr.register(comp_arithmetic_plus());
		if(pr.shouldReturn()) return pr;
		
		while(matches(TokenType.DOUBLE_EQUALS, TokenType.NOT_EQUAL, TokenType.LESS_THAN, TokenType.LESS_OR_EQUAL, TokenType.GREATER_THAN, TokenType.GREATER_OR_EQUAL)) {
			Token op = currentToken;
			advance(pr);
			
			Node right = pr.register(comp_arithmetic_plus());
			if(pr.shouldReturn()) return pr;
			
			left = new BinaryOperationNode(BinaryOperationType.get(op), left, right, since(sequence));
		}
		
		return pr.success(left);
	}
	
	private ParseResult comp_unary() {
		ParseResult pr = pr();
		
		if(matches("not", TokenType.KEYWORD)) {
			Sequence sequence = sequence();
			
			advance(pr);
			
			Node expr = pr.register(comp_unary());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(new UnaryOperationNode(UnaryOperationType.NOT, expr, since(sequence)));
		}
		
		Node expr = pr.register(comp_comparison());
		if(pr.shouldReturn()) return pr;
		
		return pr.success(expr);
	}
	
	private ParseResult comp_binop_AND() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Node left = pr.register(comp_unary());
		
		while(matches("and", TokenType.KEYWORD)) {
			advance(pr);
			
			Node right = pr.register(comp_unary());
			if(pr.shouldReturn()) return pr;
			
			left = new BinaryBooleanOperationNode(BinaryBooleanOperationType.AND, left, right, since(sequence));
		}
		
		return pr.success(left);
	}
	
	private ParseResult comp_binop_OR() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Node left = pr.register(comp_binop_AND());
		
		while(matches("or", TokenType.KEYWORD)) {
			advance(pr);
			
			Node right = pr.register(comp_binop_AND());
			if(pr.shouldReturn()) return pr;
			
			left = new BinaryBooleanOperationNode(BinaryBooleanOperationType.OR, left, right, since(sequence));
		}
		
		return pr.success(left);
	}
	
	private ParseResult comp_binop_nullish() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Node left = pr.register(comp_binop_OR());
		
		while(matches(TokenType.DOUBLE_QUESTION_MARK)) {
			advance(pr);
			
			Node right = pr.register(comp_binop_OR());
			if(pr.shouldReturn()) return pr;
			
			left = new NullishOperationNode(left, right, since(sequence));
		}
		
		return pr.success(left);
	}
	
	private ParseResult comp_ternop() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		Node expr = pr.register(comp_binop_nullish());
		if(pr.shouldReturn()) return pr;
		
		while(matches(TokenType.QUESTION_MARK)) {
			
			advance(pr);
			
			Node ifTrue = pr.register(comp_binop_nullish());
			if(pr.shouldReturn()) return pr;
			
			check(pr, TokenType.COLON);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			Node ifFalse = pr.register(comp_binop_nullish());
			if(pr.shouldReturn()) return pr;
			
			expr = new TernaryOperationNode(expr, ifTrue, ifFalse, since(sequence));
		}
		
		return pr.success(expr);
	}
	
//	private ParseResult expression() {
//		ParseResult pr = pr();
//		
//		Node expr = pr.register(comp_ternop());
//		if(pr.shouldReturn()) return pr;
//		
//		return pr.success(expr);
//	}
	
	private ParseResult expression() {
		return comp_ternop();
	}
	
	private ParseResult statement() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		if(matches("var", TokenType.KEYWORD)) {
			Node varDeclaration = pr.register(var_declaration());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(varDeclaration);
		} else if(matches("return", TokenType.KEYWORD)) {
			advance(pr);
			Node expression = pr.tryRegister(expression());
			
			if(pr.shouldHandleTried()) return pr.handleTried(new ReturnNode(expression, since(sequence)));
			reverse(pr);
			
			return pr.success(new ReturnNode(expression, since(sequence)));
		} else if(matches("break", TokenType.KEYWORD)) {
			advance(pr);
			return pr.success(new BreakNode(sequence));
		} else if(matches("continue", TokenType.KEYWORD)) {
			advance(pr);
			return pr.success(new ContinueNode(sequence));
		} else if(matches("library", TokenType.KEYWORD)) {
			advance(pr);
			
			Token token = extract(pr, TokenType.STRING, TokenType.IDENTIFIER);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			return pr.success(new SingleLibraryImportNode(token.content(), token.type() == TokenType.IDENTIFIER, since(sequence)));
		} else if(matches("import", TokenType.KEYWORD)) {
			advance(pr);
			
			Token token = extract(pr, TokenType.STRING);
			if(pr.shouldReturn()) return pr;
			advance(pr);
			
			return pr.success(new SingleImportNode(token.content(), since(sequence)));
		}
//		else if(matches("class", TokenType.KEYWORD)) {
//			Node classDeclaration = pr.register(class_declaration());
//			if(pr.shouldReturn()) return pr;
//			
//			return pr.success(classDeclaration);
//		}
//		else if(matches("enum", TokenType.KEYWORD)) {
//			Node enumDeclaration = pr.register(enum_declaration());
//			if(pr.shouldReturn()) return pr;
//			
//			return pr.success(enumDeclaration);
//		}
		else if(matches("assert", TokenType.KEYWORD)) {
			Node assertStatement = pr.register(assert_statement());
			if(pr.shouldReturn()) return pr;
			
			return pr.success(assertStatement);
		}
		
		Node expression = pr.register(expression());
		if(pr.shouldReturn()) return pr;
		
		return pr.success(expression);
	}
	
	private ParseResult boundedMultiStatements() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		check(pr, TokenType.LEFT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		advance(pr);
		
		List<Node> nodes = new ArrayList<>();
		
		while(!matches(TokenType.RIGHT_CURLY_BRACKET, TokenType.END_OF_FILE)) {
			Node node = pr.register(statement());
			if(pr.shouldReturn()) return pr;
			
			nodes.add(node);
			while(matches(TokenType.DELIMITER))
				advance(pr);
		}
		
		
		check(pr, TokenType.RIGHT_CURLY_BRACKET);
		if(pr.shouldReturn()) return pr;
		sequence = since(sequence);
		advance(pr);
		
		return pr.success(new MultiStatementsNode(nodes.toArray(new Node[nodes.size()]), sequence));
	}
	
	private ParseResult fileStatements() {
		ParseResult pr = pr();
		Sequence sequence = sequence();
		
		List<Node> nodes = new ArrayList<>();
		
		while(!matches(TokenType.END_OF_FILE)) {
			Node node = pr.register(statement());
			if(pr.shouldReturn()) return pr;
			
			nodes.add(node);
			
			while(matches(TokenType.DELIMITER))
				advance(pr);
		}
		
		return pr.success(new FileStatementsNode(nodes.toArray(new Node[nodes.size()]), since(sequence)));
	}
	
	public ParseResult parse() {
		return fileStatements();
	}

	private void advance(ParseResult pr) {
		if(currentTokenIndex >= tokens.size()-1)
			return;
		this.currentTokenIndex++;
		this.currentToken = tokens.get(currentTokenIndex);
		pr.registerAdvance();
	}
	
	private void reverse(ParseResult pr) {
		this.currentTokenIndex -= pr.getReverseCount();
		this.currentToken = tokens.get(currentTokenIndex);
		pr.resetReverseCount();
	}
	
	private boolean matches(TokenType type) { return currentToken.matches(type); }
	private boolean matches(TokenType... types) { return currentToken.matches(types); }
	private boolean matches(String content, TokenType type) { return currentToken.matches(content, type); } 
	private boolean matches(TokenType type, String... content) { return currentToken.matches(type) && currentToken.matches(content); } 
	
	private void check(ParseResult pr, TokenType type) {
		if(!matches(type))
			pr.failure(new SyntaxError(currentToken, type.name()));
	}
	
	private void check(ParseResult pr, TokenType... types) {
		if(!matches(types))
			pr.failure(new SyntaxError(currentToken, Arrays.toString(types)));
	}
	
	private void check(ParseResult pr, String content, TokenType type) {
		if(!matches(content, type))
			pr.failure(new SyntaxError(currentToken, type.name() + "("+content+")"));
	}
	
	public void reset() {
		this.currentTokenIndex = 0;
		this.currentToken = tokens.get(0);
	}
	
	private Token extract(ParseResult pr, TokenType expectedType) {
		check(pr, expectedType);
		return currentToken;
	}
	
	private Token extract(ParseResult pr, TokenType... expectedTypes) {
		check(pr, expectedTypes);
		return currentToken;
	}
	
	private ParseResult pr() { return new ParseResult(); }
	private Sequence sequence() { return currentToken.sequence(); }
	
	private Sequence since(Sequence sequence) { return sequence.join(sequence()); }
	
}
