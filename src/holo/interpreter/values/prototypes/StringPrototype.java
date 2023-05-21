package holo.interpreter.values.prototypes;

import holo.errors.WrongTypeError;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.primitives.CharValue;
import holo.interpreter.values.primitives.IntegerValue;
import holo.interpreter.values.primitives.StringValue;

public class StringPrototype  {

	public static final Prototype<StringValue> PROTOTYPE;
	
	static {
		PROTOTYPE = new Prototype<>("String");
		
		PROTOTYPE.addFunction("length", (self, runtime, args) -> {
			return runtime.buffer(IntegerValue.get(self.length()));
		});
		
		PROTOTYPE.addFunction("charAt", (self, runtime, args) -> {
			if(args[0] instanceof INumber num)
				return runtime.buffer(new CharValue(self.getValue().charAt(num.getInteger())));
			return runtime.failure(new WrongTypeError("number", args[0].toString(), null));
		}, "index");
		
//		PROTOTYPE.addFunction("size", 0, (self, args) -> {
//			return new RuntimeResult(new IntegerValue(self.getElements().size()));
//		});
	}
	
}
