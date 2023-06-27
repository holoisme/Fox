package holo.interpreter.imports;

import holo.interpreter.contexts.Context;
import holo.interpreter.values.functions.BuiltInFunctionValue;
import holo.interpreter.values.primitives.FloatValue;
import holo.interpreter.values.primitives.IntegerValue;

public class StandardLibrary implements Library {
	
	public void populate(Context fileContext) {
		fileContext.addBuiltInFunction(new BuiltInFunctionValue("print", (host, context, interpreter, args) -> {
			interpreter.print(args[0].toString());
			return args[0];
		}, "ln"));
		
		fileContext.addBuiltInFunction(new BuiltInFunctionValue("random", (host, context, interpreter, args) -> {
			return new FloatValue((float) Math.random());
		}));
		
		fileContext.addBuiltInFunction(new BuiltInFunctionValue("time", (host, context, interpreter, args) -> {
			return IntegerValue.get((int) System.currentTimeMillis());
		}));
	}

	public String name() {
		return "lib-Standard";
	}

}
