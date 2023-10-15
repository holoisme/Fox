package holo.interpreter.values.prototypes;

import holo.interpreter.values.objects.EnumEntryValue;
import holo.interpreter.values.primitives.StringValue;
import holo.interpreter.values.primitives.numbers.IntegerValue;

public class EnumEntryPrototype {
	
	public static final Prototype<EnumEntryValue> PROTOTYPE;
	
	static {
		PROTOTYPE = new Prototype<>("List");
		
		PROTOTYPE.addFunction("name", (self, args) -> {
			return new StringValue(self.name());
		});
		
		PROTOTYPE.addFunction("ordinal", (self, args) -> {
			return IntegerValue.get(self.ordinal());
		});
	}
	
}
