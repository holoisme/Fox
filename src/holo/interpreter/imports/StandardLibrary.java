package holo.interpreter.imports;

import java.util.Scanner;

import holo.interpreter.contexts.Context;
import holo.interpreter.values.functions.BuiltInFunctionValue;
import holo.interpreter.values.interfaces.INumber;
import holo.interpreter.values.portal.FoxPortal;
import holo.interpreter.values.primitives.ErrorClassValue;
import holo.interpreter.values.primitives.StringValue;
import holo.interpreter.values.primitives.numbers.FloatValue;
import holo.interpreter.values.primitives.numbers.IntegerValue;

public class StandardLibrary implements Library {
	
	public void populate(Context fileContext) {
		fileContext.addBuiltInFunction(new BuiltInFunctionValue("print", (host, context, interpreter, seq, args) -> {
			interpreter.print(args[0].toString(interpreter, seq));
			return args[0];
		}, "ln"));
		
		fileContext.setToThis("Error", ErrorClassValue.INSTANCE);
		
		fileContext.addBuiltInFunction(new BuiltInFunctionValue("random", (host, context, interpreter, seq, args) -> {
			return new FloatValue((float) Math.random());
		}));
		
		fileContext.addBuiltInFunction(new BuiltInFunctionValue("time", (host, context, interpreter, seq, args) -> {
			return IntegerValue.get((int) System.currentTimeMillis());
		}));
		
		fileContext.addBuiltInFunction(new BuiltInFunctionValue("scanner", (host, context, interpreter, seq, args) -> {
			return FoxPortal.of(new Scanner(System.in));
		}));
		
		fileContext.addBuiltInFunction(new BuiltInFunctionValue("bin", (host, context, interpreter, seq, args) -> {
			return new StringValue( Integer.toBinaryString( ((INumber)args[0]).getInteger() ) );
		}, "num"));
		
	}

	public String name() {
		return "lib-Standard";
	}

}
