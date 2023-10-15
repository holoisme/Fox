package holo.interpreter.values.functions;

import holo.errors.RuntimeError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.nodes.ReflectionUtils;
import holo.interpreter.nodes.calls.SuperConstructorCall;
import holo.interpreter.nodes.helpers.args.NamedNode;
import holo.interpreter.nodes.helpers.args.NamedValue;
import holo.interpreter.nodes.helpers.args.ObligatoryDefinitionArgument;
import holo.interpreter.nodes.helpers.args.OptionalDefinitionArgument;
import holo.interpreter.nodes.statements.ReturnNode;
import holo.interpreter.transcendental.TError;
import holo.interpreter.transcendental.TReturn;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IClass;
import holo.lang.lexer.Sequence;

public class ConstructorValue extends BaseFunctionValue {
	
	private final Node body;
	private final SuperConstructorCall superCall;
	
	private ConstructorValue cachedConstructor;
	
	public ConstructorValue(ObligatoryDefinitionArgument[] regularArguments, OptionalDefinitionArgument[] optionalArguments, Node body, SuperConstructorCall superCall) {
		super(regularArguments, optionalArguments);
		this.body = body;
		this.superCall = superCall;
		
		this.cachedConstructor = null;
	}

	public Value constructInside(IClass classValue, Context insideContext, Interpreter interpreter, Sequence sequence, Value[] args, NamedValue[] optionalArguments) {
		final Context callContext = new SimpleContext("constructor", insideContext, true);
		for(int i = 0; i < regularArguments.length; i++)
			callContext.setToThis(regularArguments[i].name(), args[i]);
		
		final IClass superClass = classValue.getSuperClass();
		
		if(superClass != null) {
			final int argsLength = superCall == null ? 0 : superCall.args().length;
			
			if(cachedConstructor == null) {
				if((cachedConstructor = superClass.getConstructor(argsLength)) == null)
					throw new TError(new RuntimeError("Not found a super constructor with " + argsLength + " arguments", body.sequence()));
			}
			
			if(superCall == null) {
				superClass.construct(cachedConstructor, insideContext, interpreter, sequence, null, null);
			} else {
				final Node[] superArgs = superCall.args();
				final NamedNode[] superOptionalArgs = superCall.optionalArguments();
				
				final Value[] superArgsValue = new Value[superArgs.length];
				final NamedValue[] superOptionalArgsValue = new NamedValue[superOptionalArgs.length];
				
				for(int i = 0; i < superArgs.length; i++)
					superArgsValue[i] = superArgs[i].interpret(callContext, interpreter);
				for(int i = 0; i < superOptionalArgs.length; i++)
					superOptionalArgsValue[i] = new NamedValue(superOptionalArgs[i].name(), superOptionalArgs[i].node().interpret(callContext, interpreter));
				
				superClass.construct(cachedConstructor, insideContext, interpreter, superCall.sequence(), superArgsValue, superOptionalArgsValue);
			}
		} else if(superCall != null) {
			if(superClass == null)
				throw new TError(new RuntimeError("This is not a sub-class", superCall.sequence()));
		}
		
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
		throw new TError(new RuntimeError("Not supposed to be called directly.", sequence));
	}
	
	public String toString() {
		return "constructor("+ReflectionUtils.toString(regularArguments)+") " + (body instanceof ReturnNode n ? n.expression() : body);
	}
	
	public Node getBody() {
		return body;
	}

}
