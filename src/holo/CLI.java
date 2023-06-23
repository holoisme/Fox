package holo;

import java.io.File;

import holo.interpreter.Interpreter;

public class CLI {
	
	public static void main(String[] args) {
		Interpreter interpreter = new Interpreter(new File("./").toPath());
		
		interpreter.setDebug(false);
		interpreter.setPerformance(false);
		
		interpreter.execute(args[0]);
	}
	
}
