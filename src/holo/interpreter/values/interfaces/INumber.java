package holo.interpreter.values.interfaces;

public interface INumber {
	
	public int getInteger();
	public float getFloat();
	public double getDouble();
	
	public boolean isInteger();
	
	public default boolean isFloat() { return numberType() == NumberType.FLOAT; }
	public default boolean isDouble() { return numberType() == NumberType.DOUBLE; }
	
	public NumberType numberType();
	
	public static enum NumberType {
		INTEGER, FLOAT, DOUBLE, BYTE, SHORT, LONG, CHAR
	}
	
}
