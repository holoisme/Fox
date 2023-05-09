package holo.interpreter;

import holo.interpreter.values.Value;

public class InterpreterUtils {
	
	private InterpreterUtils() {}
	
	public static boolean checkArgs(Value[] args, Class<?>... classes) {
		for(int i = 0; i < Math.max(args.length, classes.length); i++)
			if(!classes[i].isInstance(args[i]))
				return false;
		return true;
	}
	
}
