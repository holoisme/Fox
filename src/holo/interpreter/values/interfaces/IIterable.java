package holo.interpreter.values.interfaces;

import holo.interpreter.values.Value;

public interface IIterable {
	
	public Value elementAt(int index);
	public boolean hasReachedEnd(int index);
	
}
