package holo.interpreter.values.prototypes;

import java.util.ArrayList;

import holo.interpreter.values.objects.EnumClassValue;
import holo.interpreter.values.primitives.ListValue;
import holo.interpreter.values.primitives.numbers.IntegerValue;

public class EnumClassPrototype {
	
	public static final Prototype<EnumClassValue> PROTOTYPE;
	
	static {
		PROTOTYPE = new Prototype<>("Enum class");
		
		PROTOTYPE.addFunction("values", (self, args) -> {
			return new ListValue( new ArrayList<>(self.getEntries().values()) );
		});
		
		PROTOTYPE.addFunction("length", (self, args) -> {
			return IntegerValue.get(self.getEntries().size());
		});
	}
	
}
