package holo.interpreter.values.objects;

import holo.interpreter.contexts.Context;
import holo.interpreter.values.Value;
import holo.interpreter.values.prototypes.EnumEntryPrototype;

public class EnumEntryValue extends ConstructedObjectValue {

	private String name;
	private int ordinal;
	
	public EnumEntryValue(String enumClassName, Context enumClassDefiningContext, String name, int ordinal) {
		super(enumClassName, enumClassDefiningContext);
		this.name = name;
		this.ordinal = ordinal;
	}
	
	@Override
	public Value pointGet(String key) {
		return table.getOrDefault(key, EnumEntryPrototype.PROTOTYPE.get(key));
	}
	
	public String name() { return name; }
	public int ordinal() { return ordinal; }
	
	public String toString() { return name; }
	
}
