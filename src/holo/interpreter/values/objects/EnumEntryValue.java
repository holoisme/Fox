package holo.interpreter.values.objects;

import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IClass;
import holo.interpreter.values.primitives.StringValue;
import holo.interpreter.values.primitives.numbers.IntegerValue;

public class EnumEntryValue extends ConstructedObjectValue {

	private final String name;
	private final int ordinal;
	
	public EnumEntryValue(IClass enumClass, String name, int ordinal) {
		super(enumClass);
		this.name = name;
		this.ordinal = ordinal;
		fields.put("name", new StringValue(name));
		fields.put("ordinal", IntegerValue.get(ordinal));
	}
	
//	@Override
//	public Value pointGet(String key, Sequence sequence) {
//		return fields.getOrDefault(key, EnumEntryPrototype.PROTOTYPE.get(key));
//	}
	
	@Override
	public Value typeOf() { return classValue.instanceTypeOf(); }
	
	public String name() { return name; }
	public int ordinal() { return ordinal; }
	
	public String toString() { return name; }
	
}
