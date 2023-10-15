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

public record WhileNode(Node condition, Node body, Sequence sequence) implements Node {
	
	public String toString() {
		return "while ("+condition+"): " + body;
	}
	
	public Value interpret(Context parentContext, Interpreter interpreter) {
		final IterationContext whileContext = new IterationContext(parentContext);
		
		if(body instanceof MultiStatementsNode) {
			while(condition.interpret(whileContext, interpreter).isTrue()) {
				try {
					body.interpret(whileContext, interpreter);
				} catch(TBreak t) {
					break;
				} catch(TContinue t) {
					continue;
				}
			}
			
			return Value.UNDEFINED;
		} else {
			final List<Value> computedValues = new ArrayList<>();
			
			while(condition.interpret(whileContext, interpreter).isTrue())
				computedValues.add(body.interpret(whileContext, interpreter));
			
			return new ListValue(computedValues);
		}
	}
	
}