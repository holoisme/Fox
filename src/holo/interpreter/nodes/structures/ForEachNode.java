package holo.interpreter.nodes.structures;

import java.util.ArrayList;
import java.util.List;

import holo.errors.NotIterableError;
import holo.interpreter.Interpreter;
import holo.interpreter.RuntimeResult;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.SimpleContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IIterable;
import holo.interpreter.values.primitives.ListValue;
import holo.lang.lexer.Sequence;

public record ForEachNode(String varName, Node list, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "for("+varName + " in "+list+"): " + body;
	}
	
	public RuntimeResult interpret(Context parentContext, Interpreter interpreter, RuntimeResult onGoingRuntime) {
		RuntimeResult rt = new RuntimeResult();
		
		Context foreachContext = new SimpleContext("Foreach", parentContext);
		
		Value valueToGoTrough = rt.register(list.interpret(parentContext, interpreter, rt), list.sequence());
		if(rt.shouldReturn()) return rt;
		
		if(!(valueToGoTrough instanceof IIterable))
			return rt.failure(new NotIterableError(valueToGoTrough, sequence));
		
		IIterable iterable = (IIterable) valueToGoTrough;
		
		boolean shortForm = !(body instanceof MultiStatementsNode);
		if(shortForm) {
			List<Value> computedValues = new ArrayList<>();
			int index = 0;
			
			while(!iterable.hasReachedEnd(index)) {
				foreachContext.setToThis(varName, iterable.elementAt(index));
				
				Value bodyValue = rt.register(body.interpret(foreachContext, interpreter, rt), body.sequence());
				if(rt.shouldReturn()) return rt;
				
				computedValues.add(bodyValue);
				
				index++;
			}
			
			return rt.success(new ListValue(computedValues));
		} else {
			int index = 0;
			
			while(!iterable.hasReachedEnd(index)) {
				foreachContext.setToThis(varName, iterable.elementAt(index));
				
				rt.register(body.interpret(foreachContext, interpreter, rt), sequence);
				
				if(rt.hasError() || rt.hasReturnValue()) return rt;
				
				if(rt.encounteredBreak()) {
					rt.disableBreak();
					break;
				}
				
				index++;
				
				if(rt.encounteredContinue()) {
					rt.disableContinue();
					continue;
				}
			}
			
			return rt.success(Value.NULL);
		}
	}
	
}