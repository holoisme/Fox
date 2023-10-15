package holo.interpreter.types;

public enum CastingType {
	
	BYTE("byte"), CHARACTER("char"), SHORT("short"), INTEGER("int"), LONG("long"),
	FLOAT("float"), DOUBLE("double"),
	STRING("string"), BOOLEAN("boolean");
	
	private String symbol;
	
	private CastingType(String symbol) {
		this.symbol = symbol;
	}
	
	public String toString() { return name(); }
	public String getSymbol() { return symbol; }
	
	public static CastingType get(String type) {
		for(CastingType t:values())
			if(type.equals(t.getSymbol()))
				return t;
		
		return null;
	}
	
}
