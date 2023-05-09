package holo.interpreter;

public class InterpreterLegacy {
	
//	private InterpreterLegacy() {}
//	
//	public static RuntimeResult resolve(Node node, Context parentContext) {
//		if(node instanceof IntegerNode n) return new RuntimeResult(new IntegerValue(n.value()));
//		else if(node instanceof FloatNode n) return new RuntimeResult(new FloatValue(n.value()));
//		else if(node instanceof StringNode n) return new RuntimeResult(new StringValue(n.value()));
//		else if(node instanceof ListNode n) return resolveListNode(n, parentContext);
//		else if(node instanceof ObjectNode n) return resolveObjectNode(n, parentContext);
//		else if(node instanceof DefaultValueNode n) return new RuntimeResult(n.value());
//		else if(node instanceof ThisNode n) return resolveThisNode(n, parentContext);
//		
//		else if(node instanceof VarAccessNode n) return resolveVarAccessNode(n, parentContext);
//		else if(node instanceof VarDeclarationNode n) return resolveVarDeclarationNode(n, parentContext);
//		else if(node instanceof VarAssignmentNode n) return resolveVarAssignmentNode(n, parentContext);
//		else if(node instanceof VarPointAccessNode n) return resolveVarPointAccessNode(n, parentContext);
//		else if(node instanceof VarArrayAccessNode n) return resolveVarArrayAccessNode(n, parentContext);
//		else if(node instanceof VarAssignOperationNode n) return resolveVarAssignOperationNode(n, parentContext);
//		else if(node instanceof VarAssignQuickOperationNode n) return resolveVarAssignQuickOperationNode(n, parentContext);
//		
//		else if(node instanceof CallNode n) return resolveCallNode(n, parentContext);
//		else if(node instanceof MultiStatementsNode n) return resolveMultiStatementsNode(n, parentContext);
//		
//		else if(node instanceof UnaryOperationNode n) return resolveUnaryOperationNode(n, parentContext);
//		else if(node instanceof BinaryOperationNode n) return resolveBinaryOperationNode(n, parentContext);
//		else if(node instanceof TernaryOperationNode n) return resolveTernaryOperationNode(n, parentContext);
//		
//		System.err.println("UNKNOWN NODE " + node.getClass().getSimpleName() + " " + node);
//		return null;
//	}
//
//	public static RuntimeResult resolveVarDeclarationNode(VarDeclarationNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		if(parentContext.contains(node.varName()))
//			return rt.failure(new AlreadyExistingVariableError(node.varName(), node.sequence()));
//		
//		Value value = rt.register(resolve(node.expression(), parentContext), node.sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		parentContext.setToThis(node.varName(), value);
//		return rt.success(value);
//	}
//	
//	public static RuntimeResult resolveVarAssignmentNode(VarAssignmentNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Node access = node.access();
//		
//		Value expression = rt.register(resolve(node.expression(), parentContext), node.expression().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		if(access instanceof VarAccessNode van) {
//			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
//			if(contextThatDefinedVar == null)
//				return rt.failure(new NoSuchVariableError(van.varName(), van.sequence()));
//			contextThatDefinedVar.setToThis(van.varName(), expression);
//			
//			return rt.success(expression);
//		} else if(access instanceof VarPointAccessNode || access instanceof VarArrayAccessNode) {
//			Value hostValue = rt.register(resolve(access, parentContext), access.sequence());
//			
//			if(access instanceof VarPointAccessNode vpa) {
//				Value returnedValue = hostValue.pointSet(vpa.varName(), expression);
//				if(returnedValue == null)
//					return rt.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
//				
//				return rt.success(returnedValue);
//			} else if(access instanceof VarArrayAccessNode vpa) {
//				Value index = rt.register(resolve(vpa.index(), parentContext), vpa.access().sequence());
//				if(rt.shouldReturn()) return rt;
//				
//				Value returnedValue = hostValue.arraySet(index, expression);
//				if(returnedValue == null)
//					return rt.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
//				
//				return rt.success(returnedValue);
//			}
//		}
//		
//		return rt.failure(new CannotAccessError(node.access().getClass().getSimpleName(), node.access().sequence()));
//	}
//	
//	public static RuntimeResult resolveVarAccessNode(VarAccessNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Value value = parentContext.get(node.varName());
//		if(value == null)
//			return rt.failure(new NoSuchVariableError(node.varName(), node.sequence()));
//		
//		return rt.success(value);
//	}
//	
//	public static RuntimeResult resolveVarPointAccessNode(VarPointAccessNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Value hostValue = rt.register(resolve(node.access(), parentContext), node.access().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		Value pointGetValue = hostValue.pointGet(node.varName());
//		if(pointGetValue == null)
//			return rt.failure(new CannotAccessError(hostValue.typeName(), node.access().sequence()));
//		
//		return rt.success(pointGetValue);
//	}
//	
//	public static RuntimeResult resolveVarArrayAccessNode(VarArrayAccessNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Value hostValue = rt.register(resolve(node.access(), parentContext), node.access().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		Value index = rt.register(resolve(node.index(), parentContext), node.index().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		Value arrayGetValue = hostValue.arrayGet(index);
//		if(arrayGetValue == null)
//			return rt.failure(new CannotAccessError(hostValue.typeName(), node.access().sequence()));
//		
//		return rt.success(arrayGetValue);
//	}
//	
//	public static RuntimeResult resolveVarAssignOperationNode(VarAssignOperationNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Node access = node.access();
//		BinaryOperationType operation = node.operation();
//		
//		Value expression = rt.register(resolve(node.expression(), parentContext), node.expression().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		if(operation == BinaryOperationType.DIVIDE &&
//			expression.equalTo(IntegerValue.ZERO)) {
//			return rt.failure(new RuntimeError("Division by zero", node.expression().sequence()));
//		}
//		
//		if(access instanceof VarAccessNode van) {
//			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
//			if(contextThatDefinedVar == null)
//				return rt.failure(new NoSuchVariableError(van.varName(), van.sequence()));
//			
//			Value currentValue = contextThatDefinedVar.get(van.varName());
//			Value result = currentValue.binaryOperation(operation, expression);
//			
//			if(result == null)
//				return rt.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), node.sequence()));
//			
//			contextThatDefinedVar.setToThis(van.varName(), result);
//			
//			return rt.success(result);
//		} else if(access instanceof VarPointAccessNode || access instanceof VarArrayAccessNode) {
//			Value hostValue = rt.register(resolve(access, parentContext), access.sequence());
//			
//			if(access instanceof VarPointAccessNode vpa) {
//				Value currentValue = hostValue.pointGet(vpa.varName());
//				if(currentValue == null)
//					return rt.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
//				
//				Value result = currentValue.binaryOperation(operation, expression);
//				
//				if(result == null)
//					return rt.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), node.sequence()));
//				
//				return rt.success(result);
//			} else if(access instanceof VarArrayAccessNode vpa) {
//				Value index = rt.register(resolve(vpa.index(), parentContext), vpa.access().sequence());
//				if(rt.shouldReturn()) return rt;
//				
//				Value currentValue = hostValue.arrayGet(index);
//				if(currentValue == null)
//					return rt.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
//				
//				Value result = currentValue.binaryOperation(operation, expression);
//				
//				if(result == null)
//					return rt.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), node.sequence()));
//				
//				return rt.success(result);
//			}
//		}
//		
//		return rt.failure(new CannotAccessError(node.access().getClass().getSimpleName(), node.access().sequence()));
//	}
//	
//	public static RuntimeResult resolveVarAssignQuickOperationNode(VarAssignQuickOperationNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Node access = node.access();
//		QuickOperationType operation = node.operation();
//		
//		if(access instanceof VarAccessNode van) {
//			Context contextThatDefinedVar = parentContext.getFirstParentThatHasKey(van.varName());
//			if(contextThatDefinedVar == null)
//				return rt.failure(new NoSuchVariableError(van.varName(), van.sequence()));
//			
//			Value currentValue = contextThatDefinedVar.get(van.varName());
//			Value result = currentValue.quickOperation(operation);
//			
//			if(result == null)
//				return rt.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), node.sequence()));
//			
//			contextThatDefinedVar.setToThis(van.varName(), result);
//			
//			return rt.success(result);
//		} else if(access instanceof VarPointAccessNode || access instanceof VarArrayAccessNode) {
//			Value hostValue = rt.register(resolve(access, parentContext), access.sequence());
//			
//			if(access instanceof VarPointAccessNode vpa) {
//				Value currentValue = hostValue.pointGet(vpa.varName());
//				if(currentValue == null)
//					return rt.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
//				
//				Value result = currentValue.quickOperation(operation);
//				
//				if(result == null)
//					return rt.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), node.sequence()));
//				
//				return rt.success(result);
//			} else if(access instanceof VarArrayAccessNode vpa) {
//				Value index = rt.register(resolve(vpa.index(), parentContext), vpa.access().sequence());
//				if(rt.shouldReturn()) return rt;
//				
//				Value currentValue = hostValue.arrayGet(index);
//				if(currentValue == null)
//					return rt.failure(new CannotAccessError(hostValue.typeName(), access.sequence()));
//				
//				Value result = currentValue.quickOperation(operation);
//				
//				if(result == null)
//					return rt.failure(new IllegalOperationError(operation.toString(), currentValue.typeName(), node.sequence()));
//				
//				return rt.success(result);
//			}
//		}
//		
//		return rt.failure(new CannotAccessError(node.access().getClass().getSimpleName(), node.access().sequence()));
//	}
//	
//	public static RuntimeResult resolveListNode(ListNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		List<Value> elements = new ArrayList<>();
//		
//		for(Node elementNode: node.nodes()) {
//			Value element = rt.register(resolve(elementNode, parentContext), elementNode.sequence());
//			if(rt.shouldReturn()) return rt;
//			
//			elements.add(element);
//		}
//		
//		return rt.success(new ListValue(elements));
//	}
//	
//	public static RuntimeResult resolveObjectNode(ObjectNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		Map<String, Value> map = new HashMap<>();
//		
//		for(ObjectStatementSequence objectStatement: node.statements()) {
//			Value element = rt.register(resolve(objectStatement.expression(), parentContext), objectStatement.expression().sequence());
//			if(rt.shouldReturn()) return rt;
//			
//			map.put(objectStatement.name(), element);
//		}
//		
//		return rt.success(new ObjectValue(map, parentContext));
//	}
//	
//	public static RuntimeResult resolveCallNode(CallNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Value access = rt.register(resolve(node.access(), parentContext), node.access().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		Node[] argNodes = node.args();
//		
//		Value[] args = new Value[argNodes.length];
//		for(int i = 0; i < args.length; i++) {
//			args[i] = rt.register(resolve(argNodes[i], parentContext), argNodes[i].sequence());
//			if(rt.shouldReturn()) return rt;
//		}
//		
//		RuntimeResult callResult = access.call(args);
//		if(callResult == null)
//			return rt.failure(new CannotCallError(access.typeName(), node.access().sequence()));
//		
//		Value returnedValue = rt.register(callResult, node.sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		return rt.success(returnedValue);
//	}
//	
//	public static RuntimeResult resolveMultiStatementsNode(MultiStatementsNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		for(Node statementNode:node.statements()) {
//			rt.register(resolve(statementNode, parentContext), statementNode.sequence());
//			if(rt.shouldReturn()) return rt;
//		}
//		
//		return rt.success(Value.NULL);
//	}
//	
//	public static RuntimeResult resolveUnaryOperationNode(UnaryOperationNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Value expression = rt.register(resolve(node.node(), parentContext), node.node().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		Value result = expression.unaryOperation(node.operation());
//		if(result == null)
//			return rt.failure(new IllegalOperationError(node.operation().toString(), expression.typeName(), node.sequence()));
//		
//		return rt.success(result);
//	}
//	
//	public static RuntimeResult resolveBinaryOperationNode(BinaryOperationNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Value left = rt.register(resolve(node.left(), parentContext), node.left().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		Value right = rt.register(resolve(node.right(), parentContext), node.right().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		Value result = left.binaryOperation(node.operation(), right);
//		if(result == null)
//			return rt.failure(new IllegalOperationError(node.operation().toString(), left.typeName() + " and " + right.typeName(), node.sequence()));
//		
//		return rt.success(result);
//	}
//	
//	public static RuntimeResult resolveTernaryOperationNode(TernaryOperationNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Value condition = rt.register(resolve(node.condition(), parentContext), node.condition().sequence());
//		if(rt.shouldReturn()) return rt;
//		
//		if(condition.isTrue()) {
//			Value ifTrue = rt.register(resolve(node.ifTrue(), parentContext), node.ifTrue().sequence());
//			if(rt.shouldReturn()) return rt;
//			
//			return rt.success(ifTrue);
//		} else {
//			Value ifFalse = rt.register(resolve(node.ifFalse(), parentContext), node.ifFalse().sequence());
//			if(rt.shouldReturn()) return rt;
//			
//			return rt.success(ifFalse);
//		}
//	}
//	
//	public static RuntimeResult resolveThisNode(ThisNode node, Context parentContext) {
//		RuntimeResult rt = new RuntimeResult();
//		
//		Context firstThisableParentContext = parentContext.getFirstThisableParent();
//		if(firstThisableParentContext == null)
//			return rt.failure(new NoThisError(node.sequence()));
//		
//		return rt.success(firstThisableParentContext.thisValue());
//	}
	
}
