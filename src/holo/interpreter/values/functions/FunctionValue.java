package holo.interpreter.values.functions;

import holo.errors.IncorrectNumberOfArguments;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.nodes.statements.ReturnNode;
import holo.interpreter.transcendental.TError;
import holo.interpreter.transcendental.TReturn;
import holo.interpreter.values.Value;
import holo.lang.lexer.Sequence;

public class FunctionValue extends BaseFunctionValue {
	
	private Context definingContext;
	private Node body;
	
	public FunctionValue(ObligatoryDefinitionArgument[] regularArguments, OptionalDefinitionArgument[] optionalArguments, Context definingContext, Node body) {
		super(regularArguments, optionalArguments);
		this.definingContext = definingContext;
		this.body = body;
	}

	public Value callInside(Value host, Context insideContext, Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments) {
		Context callContext = new SimpleContext("function", insideContext, true);
		
		if(!((args == null && regularArguments.length == 0) || (regularArguments.length == args.length)))
			throw new TError(new IncorrectNumberOfArguments(regularArguments.length, args.length, "function", sequence));
		
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
		
		return Value.UNDEFINED;
	}
	
	@Override
	public Value call(Value host, Context parentContext, Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments) {
		return callInside(host, definingContext, interpreter, sequence, args, optionalArguments);
	}
	
	public String toString() {
		return "("+ReflectionUtils.toString(regularArguments)+") -> " + (body instanceof ReturnNode n ? n.expression() : body);
	}
	
	public Node getBody() {
		return body;
	}

}
