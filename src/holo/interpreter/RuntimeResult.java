package holo.interpreter;

public class RuntimeResult {
	
	private RuntimeResult() {}
	
//	private boolean encounteredContinue = false,
//					encounteredBreak = false;
//	private Value returnValue = null;
//	
//	private Value value, bufferValue;
//	private RuntimeError error;
//	
//	public RuntimeResult() {}
//	
////	public RuntimeResult(Value value) { this.value = value; }
//	
//	public Value register(RuntimeResult rt, Sequence sequence) {
//		return register(rt, "", sequence);
//	}
//	
//	public Value register(RuntimeResult rt, String traceName, Sequence sequence) {
//		if(rt.error != null) {
//			error = rt.error;
//			
//			if(sequence != null)
//				error.addToTrace(traceName, sequence);
//		}
//		
//		encounteredContinue |= rt.encounteredContinue;
//		encounteredBreak |= rt.encounteredBreak;
//		
//		if(rt.returnValue != null) returnValue = rt.returnValue;
//		if(bufferValue != null) {
//			Value b = bufferValue;
//			bufferValue = null;
//			return b;
//		}
//		
//		return rt.value;
//	}
//	
//	public RuntimeResult buffer(Value value) {
//		this.bufferValue = value;
//		return this;
//	}
//	
//	public RuntimeResult success(Value value) {
//		this.value = value;
//		return this;
//	}
//	
//	public RuntimeResult failure(RuntimeError error) {
//		this.error = error;
//		this.error.addToTrace("", error.getSequence());
//		return this;
//	}
//	
//	public boolean shouldReturn() {
//		return error != null || returnValue != null || encounteredContinue || encounteredBreak;
//	}
//	
//	public boolean encounteredContinue() { return encounteredContinue; }
//	public boolean encounteredBreak() { return encounteredBreak; }
//	public boolean encounteredReturn() { return returnValue != null; }
//	
//	public boolean hasError() { return error != null; }
//	public RuntimeError getError() { return error; }
//	
//	public boolean hasReturnValue() { return returnValue != null; }
//	
//	public void disableContinue() { encounteredContinue = false; }
//	public void disableBreak() { encounteredBreak = false; }
//	public void clearReturn() { returnValue = null; }
//	public void clearError() { error = null; }
//	
//	public RuntimeResult encounterContinue() {
//		encounteredContinue = true;
//		return this;
//	}
//	
//	public RuntimeResult encounterBreak() {
//		encounteredBreak = true;
//		return this;
//	}
//	
//	public RuntimeResult encounterReturn(Value value) {
//		returnValue = value;
//		return this;
//	}
//	
//	public Value getReturnValue() { return returnValue; }
//	
//	public String toString() {
//		return "RT " + value + ", " + error;
//	}
//	
//	public String toStringFull(String fileName) {
//		return "Runtime result" + (fileName == null?"":" of " + fileName) + " :\n" + ("Error: " + (error == null?"No error":error.toString()) + "\nValue: " + (value == null?"No value":value.toString())).indent(2);
//	}
	
}
