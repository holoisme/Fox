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

public record WhileNode(Node condition, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "while ("+condition+"): " + body;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		Context forContext = new SimpleContext("While", parentContext);
		
		boolean shortForm = !(body instanceof MultiStatementsNode);
		
		if(shortForm) {
			List<Value> computedValues = new ArrayList<>();
			
			while(rt.register(condition.interpret(forContext, interpreter, rt), condition.sequence()).isTrue()) {
				if(rt.shouldReturn()) return rt;
				
				Value bodyValue = rt.register(body.interpret(forContext, interpreter, rt), sequence);
				if(rt.shouldReturn()) return rt;
				
				computedValues.add(bodyValue);
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