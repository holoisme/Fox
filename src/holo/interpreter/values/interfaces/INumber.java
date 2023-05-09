package holo.interpreter.values.interfaces;

public interface INumber {
	
	public int getInteger();
	public float getFloat();
	
	public default boolean isInteger() { return getInteger() == getFloat(); }
	
}
