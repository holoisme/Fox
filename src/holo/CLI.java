package holo;

import java.io.File;

import holo.interpreter.Interpreter;

public class CLI {
	
	public static void main(String[] args) {
		// Interpreter interpreter = new Interpreter(new File("./math-evaluator").toPath());
		
		// interpreter.setDebug(false);
		// interpreter.setPerformance(false);
		
		// interpreter.execute("Sandbox.fox");
		
		Interpreter interpreter = new Interpreter(new File(args[0]).getParentFile().toPath());
		interpreter.setDebug(false);
		interpreter.setPerformance(false);
		interpreter.execute(args[0]);
	}
	
}
