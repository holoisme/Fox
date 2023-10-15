package holo.interpreter.values.prototypes;

import java.util.HashMap;
import java.util.Map;

import holo.interpreter.values.Value;
import holo.interpreter.values.functions.BuiltInFunctionValue;

public class Prototype<T> {
	
	private final String name;
	private final Map<String, Value> table;
	
	protected Prototype(String name) {
		this.name = name;
		this.table = new HashMap<>();
	}
	
	protected Prototype(String name, Map<String, Value> table) {
		this.name = name;
		this.table = table;
	}

	public String getName() {
		return name;
	}

	public Value get(String key) { return table.get(key); }
	
	@SuppressWarnings("unchecked")
	protected void addFunction(String functionName, PrototypeCall<T> lambda, String... arguments) {
		table.put(functionName, new BuiltInFunctionValue(functionName, (host, context, interpreter, seq, args) -> {
			return lambda.call((T) host, args);
		}, arguments));
//		table.put(functionName, new BuiltInFunctionValue(functionName, (context, interpreter, args) -> {
//			return null;
//		}, arguments));
		// table.put(functionName, new Function(functionName) etc) etc
	}
	
	public interface PrototypeCall<T> {
		public Value call(T self, Value... args);
	}
	
}
