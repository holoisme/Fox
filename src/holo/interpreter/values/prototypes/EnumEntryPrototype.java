package holo.interpreter.values.prototypes;

import holo.interpreter.values.objects.EnumEntryValue;
import holo.interpreter.values.primitives.IntegerValue;
import holo.interpreter.values.primitives.StringValue;

public class EnumEntryPrototype {
	
	public static final Prototype<EnumEntryValue> PROTOTYPE;
	
	static {
		PROTOTYPE = new Prototype<>("List");
		
		PROTOTYPE.addFunction("name", (self, runtime, args) -> {
			return runtime.buffer(new StringValue(self.name()));
		});
		
		PROTOTYPE.addFunction("ordinal", (self, runtime, args) -> {
			return runtime.buffer(new IntegerValue(self.ordinal()));
		});
	}
	
}
