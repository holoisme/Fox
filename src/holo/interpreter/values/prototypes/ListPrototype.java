package holo.interpreter.values.prototypes;

import holo.interpreter.values.primitives.ListValue;
import holo.interpreter.values.primitives.numbers.IntegerValue;

public class ListPrototype  {

	public static final Prototype<ListValue> PROTOTYPE;
	
	static {
		PROTOTYPE = new Prototype<>("List");
		
		PROTOTYPE.addFunction("size", (self, args) -> {
			return IntegerValue.get(self.size());
		});
		
		PROTOTYPE.addFunction("add", (self, args) -> {
			return self.addElement(args[0]);
		}, "val");
	}
	
}
