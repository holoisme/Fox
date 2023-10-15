package holo;

import java.io.File;

import holo.interpreter.Interpreter;
import holo.interpreter.SimpleInterpreter;

public class CLI {
	
	public static void main(String[] args) {
		Interpreter interpreter = new SimpleInterpreter(new File("./").toPath());
		
		interpreter.setDebug(false);
		interpreter.execute(args[0]);
	}
	
}
