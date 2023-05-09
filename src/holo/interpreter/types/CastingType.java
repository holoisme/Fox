package holo.interpreter.types;

public enum CastingType {
	
	INTEGER("int"), FLOAT("float"), STRING("string"), BOOLEAN("boolean");
	
	private String symbol;
	
	private CastingType(String symbol) {
		this.symbol = symbol;
	}
	
	public String toString() { return symbol; }
	
	public static CastingType get(String type) {
		if(type.equals("int")) return INTEGER;
		else if(type.equals("float")) return FLOAT;
		else if(type.equals("string")) return STRING;
		else if(type.equals("boolean")) return BOOLEAN;
		
		return null;
	}
	
}
