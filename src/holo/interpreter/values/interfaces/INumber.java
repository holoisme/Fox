package holo.interpreter.values.interfaces;

import holo.interpreter.Interpreter;
import holo.interpreter.types.BinaryOperationType;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.StringValue;
import holo.lang.lexer.Sequence;

public interface INumber {
	
	public static final StringValue T_NUMBER = new StringValue("number");
	
	int getInteger();
	Number get();
	boolean isInteger();
	
	public NumberType numberType();
	
	public Value handle(INumber left, BinaryOperationType operation, Interpreter interpreter, Sequence sequence);
	
	public static enum NumberType {
		BYTE(true, true), CHAR(true, true), SHORT(true, true), INTEGER(true, true), LONG(true, false),
		FLOAT(false, false), DOUBLE(false, false);
		
		private boolean integer, operationConvertToInteger;
		private NumberType(boolean integer, boolean operationConvertToInteger) {
			this.integer = integer;
			this.operationConvertToInteger = operationConvertToInteger;
		}
		
		public boolean isInteger() { return integer; }
		public boolean doesOperationConvertToInteger() { return operationConvertToInteger; }
	}
	
}
