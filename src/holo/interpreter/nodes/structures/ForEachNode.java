package holo.interpreter.nodes.structures;

import java.util.ArrayList;
import java.util.List;

import holo.errors.NotIterableError;
import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.IterationContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.interpreter.values.interfaces.IIterable;
import holo.interpreter.values.primitives.ListValue;
import holo.lang.lexer.Sequence;
import holo.transcendental.TBreak;
import holo.transcendental.TContinue;
import holo.transcendental.TError;

public record ForEachNode(String varName, Node list, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "for("+varName + " in "+list+"): " + body;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		IterationContext foreachContext = new IterationContext(parentContext);
		
		Value valueToGoTrough = list.interpret(parentContext, interpreter);
		
		if(!(valueToGoTrough instanceof IIterable))
			throw new TError(new NotIterableError(valueToGoTrough, sequence));
		
		IIterable iterable = (IIterable) valueToGoTrough;
		
		int index = 0;
		
		if(body instanceof MultiStatementsNode multi) {
			
			while(!iterable.hasReachedEnd(index)) {
				foreachContext.setToThis(varName, iterable.elementAt(index));
				
				try {
					multi.interpretTransparently(foreachContext, interpreter);
					foreachContext.clear();
				} catch(TBreak t) {
					break;
				} catch(TContinue t) {
					continue;
				}
			}
			
			return Value.NULL;
		} else {
			List<Value> computedValues = new ArrayList<>();
			
			while(!iterable.hasReachedEnd(index)) {
				foreachContext.setToThis(varName, iterable.elementAt(index));
				
				Value bodyValue = body.interpret(foreachContext, interpreter);
				
				computedValues.add(bodyValue);
				
				index++;
			}
			
			return new ListValue(computedValues);
		}
	}
	
}