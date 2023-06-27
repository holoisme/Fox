package holo.interpreter.values.prototypes;

import holo.errors.WrongTypeError;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.primitives.CharValue;
import holo.interpreter.values.primitives.IntegerValue;
import holo.interpreter.values.primitives.StringValue;
import holo.transcendental.TError;

public class StringPrototype  {

	public static final Prototype<StringValue> PROTOTYPE;
	
	static {
		PROTOTYPE = new Prototype<>("String");
		
		PROTOTYPE.addFunction("length", (self, args) -> {
			return IntegerValue.get(self.length());
		});
		
		PROTOTYPE.addFunction("charAt", (self, args) -> {
			if(args[0] instanceof INumber num)
				return new CharValue(self.getValue().charAt(num.getInteger()));
			throw new TError(new WrongTypeError("number", args[0].toString(), null));
		}, "index");
		
//		PROTOTYPE.addFunction("size", 0, (self, args) -> {
//			return new RuntimeResult(new IntegerValue(self.getElements().size()));
//		});
	}
	
}
