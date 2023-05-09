package holo.interpreter.values.functions;

import java.util.Arrays;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.helpers.SingleDefinitionArgument;
import holo.interpreter.values.Value;

public class FunctionValue extends BaseFunctionValue {
	
	private Context definingContext;
	private Node body;
	
	public FunctionValue(SingleDefinitionArgument[] arguments, Context definingContext, Node body) {
		super(arguments);
		this.definingContext = definingContext;
		this.body = body;
	}

	public RuntimeResult callInside(Value host, Context insideContext, Interpreter interpreter, RuntimeResult onGoingRuntime, Value... args) {
		RuntimeResult rt = new RuntimeResult();
		
		Context callContext = new SimpleContext("function", insideContext);
		for(int i = 0; i < definingArguments.length; i++)
			callContext.setToThis(definingArguments[i].name(), args[i]);
		
		rt.register(body.interpret(callContext, interpreter, rt), body.sequence());
		
		if(rt.hasError()) return rt;
		
		if(rt.hasReturnValue()) {
			Value returnedValue = rt.getReturnValue();
			rt.clearReturn();
			return rt.success(returnedValue);
		}
		
		return rt.success(Value.NULL);
	}
	
	public RuntimeResult call(Value host, Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime, Value... args) {
		return callInside(host, definingContext, interpreter, onGoingRuntime, args);
//		RuntimeResult rt = new RuntimeResult();
//		
//		Context callContext = new SimpleContext("function", definingContext);
//		for(int i = 0; i < definingArguments.length; i++)
//			callContext.setToThis(definingArguments[i].name(), args[i]);
//		
//		rt.register(body.interpret(callContext, interpreter, rt), body.sequence());
//		
//		if(rt.hasError()) return rt;
//		
//		if(rt.hasReturnValue()) {
//			Value returnedValue = rt.getReturnValue();
//			rt.clearReturn();
//			return rt.success(returnedValue);
//		}
//		
//		return rt.success(Value.NULL);
	}
	
	public String toString() {
		return "("+Arrays.toString(definingArguments)+") -> " + body;
	}
	
	public Node getBody() {
		return body;
	}

}
