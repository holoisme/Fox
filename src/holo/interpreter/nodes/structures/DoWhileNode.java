package holo.interpreter.nodes.structures;

import java.util.ArrayList;
import java.util.List;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.IterationContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.transcendental.TBreak;
import holo.interpreter.transcendental.TContinue;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.ListValue;
import holo.lang.lexer.Sequence;

public record DoWhileNode(Node condition, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "do "+body+" while ("+condition+");";
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		
		if(body instanceof MultiStatementsNode multi) {
			final IterationContext doWhileContext = new IterationContext(parentContext);
			
			do {
				try {
					multi.interpretTransparently(doWhileContext, interpreter);
					doWhileContext.clear();
				} catch(TBreak t) {
					break;
				} catch(TContinue t) {
					continue;
				}
			} while(condition.interpret(doWhileContext, interpreter).isTrue());
			
			return Value.UNDEFINED;
		} else {
			final List<Value> computedValues = new ArrayList<>();
			
			do {
				computedValues.add(body.interpret(parentContext, interpreter));
			} while(condition.interpret(parentContext, interpreter).isTrue());
			
			return new ListValue(computedValues);
		}
		
	}
	
}
