package holo.interpreter.nodes.structures;

import java.util.ArrayList;
import java.util.List;

import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.ListValue;
import holo.lang.lexer.Sequence;

public record ForNode(Node initialization, Node condition, Node step, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "for("+initialization+"; " + condition + "; " + step+"): " + body;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		Context forContext = new SimpleContext("For", parentContext);
		
		if(initialization != null) {
			rt.register(initialization.interpret(forContext, interpreter, rt), initialization.sequence());
			if(rt.shouldReturn()) return rt;
		}
		
		boolean shortForm = !(body instanceof MultiStatementsNode);
		
		if(shortForm) {
			List<Value> computedValues = new ArrayList<>();
			
			while(rt.register(condition.interpret(forContext, interpreter, rt), condition.sequence()).isTrue()) {
				if(rt.shouldReturn()) return rt;
				
				Value bodyValue = rt.register(body.interpret(forContext, interpreter, rt), sequence);
				if(rt.shouldReturn()) return rt;
				
				computedValues.add(bodyValue);
				
				rt.register(step.interpret(forContext, interpreter, rt), step.sequence());
			}
			
			return rt.success(new ListValue(computedValues));
		} else {
			while(rt.register(condition.interpret(forContext, interpreter, rt), condition.sequence()).isTrue()) {
				
				if(rt.shouldReturn()) return rt;
				
				rt.register(body.interpret(forContext, interpreter, rt), sequence);
				
				if(rt.hasError() || rt.hasReturnValue()) return rt;
				
				if(rt.encounteredBreak()) {
					rt.disableBreak();
					break;
				}
				
				rt.register(step.interpret(forContext, interpreter, rt), step.sequence());
				if(rt.encounteredContinue()) {
					rt.disableContinue();
					continue;
				}
				
				if(rt.shouldReturn()) return rt;
			}
			
			return rt.success(Value.NULL);
		}
	}
	
}