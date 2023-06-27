package holo.interpreter.values.functions;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.values.Value;
import holo.transcendental.TReturn;

public class FunctionValue extends BaseFunctionValue {
	
	private Context definingContext;
	private Node body;
	
	public FunctionValue(ObligatoryDefinitionArgument[] regularArguments, OptionalDefinitionArgument[] optionalArguments, Context definingContext, Node body) {
		super(regularArguments, optionalArguments);
		this.definingContext = definingContext;
		this.body = body;
	}

	public Value callInside(Value host, Context insideContext, Interpreter interpreter, Value[] args, NamedValue[] optionalArguments) {
		
		Context callContext = new SimpleContext("function", insideContext);
		for(int i = 0; i < regularArguments.length; i++)
			callContext.setToThis(regularArguments[i].name(), args[i]);
		if(this.optionalArguments != null && this.optionalArguments.length != 0)
			upper:
			for(OptionalDefinitionArgument opt:this.optionalArguments) {
				final String name = opt.name();
				
				for(NamedValue namedOpt:optionalArguments)
					if(namedOpt.name().equals(name)) {
						callContext.setToThis(name, namedOpt.value());
						continue upper;
					}
				
				callContext.setToThis(name, opt.defaultExpression().interpret(callContext, interpreter));
			}
		
		try {
			body.interpret(callContext, interpreter);
		} catch(TReturn t) {
			return t.value();
		}
		
		return Value.NULL;
	}
	
	public Value call(Value host, Context parentContext, Interpreter interpreter, Value[] args, NamedValue[] optionalArguments) {
		return callInside(host, definingContext, interpreter, args, optionalArguments);
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
		return "("+ReflectionUtils.toString(regularArguments)+") -> " + body;
	}
	
	public Node getBody() {
		return body;
	}

}
