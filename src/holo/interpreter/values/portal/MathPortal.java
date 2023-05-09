package holo.interpreter.values.portal;

public class MathPortal {
	
	public final double PI = Math.PI;
	
	public float cos(int value) { return (float)Math.cos(value); }
	public float cos(float value) { return (float)Math.cos(value); }
	
	public float sin(int value) { return (float)Math.sin(value); }
	public float sin(float value) { return (float)Math.sin(value); }
	
	public String typeName() {
		return "Math";
	}

}
