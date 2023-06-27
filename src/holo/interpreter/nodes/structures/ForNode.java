package holo.interpreter.nodes.structures;

import java.util.ArrayList;
import java.util.List;

import holo.interpreter.Interpreter;
import holo.interpreter.contexts.Context;
import holo.interpreter.contexts.IterationContext;
import holo.interpreter.nodes.Node;
import holo.interpreter.values.Value;
import holo.interpreter.values.primitives.ListValue;
import holo.lang.lexer.Sequence;
import holo.transcendental.TBreak;
import holo.transcendental.TContinue;

public record ForNode(Node initialization, Node condition, Node step, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "for("+initialization+"; " + condition + "; " + step+"): " + body;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		IterationContext forContext = new IterationContext(parentContext);
		
		if(initialization != null)
			initialization.interpret(forContext, interpreter);
		
		if(body instanceof MultiStatementsNode) {
			while(condition.interpret(forContext, interpreter).isTrue()) {
				
				try {
					body.interpret(forContext, interpreter);
				} catch(TBreak t) {
					break;
				} catch(TContinue t) {
					step.interpret(forContext, interpreter);
					continue;
				}
				
				step.interpret(forContext, interpreter);
			}
			
			return Value.NULL;
		} else {
			List<Value> computedValues = new ArrayList<>();
			
			while(condition.interpret(forContext, interpreter).isTrue()) {
				computedValues.add(body.interpret(forContext, interpreter));
				
				step.interpret(forContext, interpreter);
			}
			
			return new ListValue(computedValues);
		}
	}
	
}