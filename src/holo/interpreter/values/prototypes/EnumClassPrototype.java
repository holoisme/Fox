package holo.interpreter.values.prototypes;

import java.util.ArrayList;

import holo.interpreter.values.objects.EnumClassValue;
import holo.interpreter.values.primitives.IntegerValue;
import holo.interpreter.values.primitives.ListValue;

public class EnumClassPrototype {
	
	public static final Prototype<EnumClassValue> PROTOTYPE;
	
	static {
		PROTOTYPE = new Prototype<>("Enum class");
		
		PROTOTYPE.addFunction("values", (self, runtime, args) -> {
			return runtime.buffer(new ListValue( new ArrayList<>(self.getEntries().values()) ));
		});
		
		PROTOTYPE.addFunction("length", (self, runtime, args) -> {
			return runtime.buffer(new IntegerValue(self.getEntries().size()));
		});
	}
	
}
