package holo.interpreter.values.functions;

public class OperatorFunctionValue {
	
//	private final BinaryOperationType operation;
//	private final String argumentName;
//	private final Node body;
//	
//	public OperatorFunctionValue(BinaryOperationType operation, String argumentName, Node body) {
//		this.operation = operation;
//		this.argumentName = argumentName;
//		this.body = body;
//	}
//
//	public Value binaryOperation(Value argument, Context insideContext, Interpreter interpreter, Sequence sequence) {
//		final Context callContext = new SimpleContext("operator", insideContext, true);
//		callContext.setToThis(argumentName, argument);
//		
//		try {
//			body.interpret(callContext, interpreter);
//		} catch(TReturn t) {
//			return t.value();
//		}
//		
//		return Value.UNDEFINED;
//	}
//	
//	@Override
//	public String toString() {
//		return "operator " + operation.getSymbol() + "(" + argumentName + ")" + body.toString();
//	}
//	
//	public Node getBody() {
//		return body;
//	}

}
