package holo.lang.parser;

import holo.errors.FoxError;
import holo.interpreter.nodes.Node;

public class ParseResult {
	
	private Node node;
	private FoxError error;
	
	private int advanceCount = 0, reverseCount = 0;
	private boolean reverseError = false, commited = false;
	
	public Node register(ParseResult pr) {
		this.advanceCount += pr.advanceCount;
		if(pr.error != null) error = pr.error;
		return pr.node;
	}
	
	public Node tryRegister(ParseResult pr) {
		if(pr.shouldReturn() && !pr.isCommited()) {
			reverseCount = pr.advanceCount;
			reverseError = true;
			return null;
		}
		
		commited = true; // TODO VERY EXPERIMENTAL
		return register(pr);
	}
	
	public ParseResult success(Node node) {
		this.node = node;
		return this;
	}
	
	public ParseResult failure(FoxError error) {
		this.error = error;
		return this;
	}
	
	public ParseResult commit() {
		this.commited = true;
		return this;
	}
	
	public void registerAdvance() { advanceCount++; }
	public void resetReverseCount() { reverseCount = 0; reverseError = false; }
	
	public boolean shouldReturn() { return error != null; }
//	public boolean shouldReverse() { return reverseCount > 0 || reverseError; }
//	public boolean shouldHandleTried() { return (node != null || error != null) && !reverseError; }
	public boolean shouldHandleTried() { return commited || (node != null || error != null) && !reverseError; }
	
	public boolean isCommited() { return commited; }
	
	public ParseResult handleTried(Node node) {
		return error == null ? success(node) : this;
	}
	
	public int getReverseCount() { return reverseCount; }
	public FoxError getError() { return error; }
	public Node node() { return node; }
	
	public String toString() {
		return "ParseResult("+error+","+node+")";
	}
	
	public String toStringFull(String fileName) {
		return "Parsing result" + (fileName == null?"":" of " + fileName) + " :\n" + ("Error: " + (error == null?"No error":error.toString()) + "\nNode: " + (node == null?"No node":node.toString())).indent(2);
	}
}