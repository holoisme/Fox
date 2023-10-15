package holo.lang.lexer;

public record Sequence(int lineStart, int lineEnd, int rowStart, int rowEnd, String file) {
	
	public static Sequence from(int line, int row, int size, String file) {
		return new Sequence(line, line, row, row + size, file);
	}
	
	public Sequence join(Sequence other) {
		if(other == null || other == this)
			return this;
		
		final int lStart = Math.min(lineStart, other.lineStart);
		final int lEnd = Math.max(lineEnd, other.lineEnd);
		
		return lStart == lEnd ?
				new Sequence(lStart, lEnd, Math.min(rowStart, other.rowStart), Math.max(rowEnd, other.rowEnd), file) :
				new Sequence(lStart, lEnd, lStart == lineStart ? rowStart : other.rowStart, lEnd == lineEnd ? rowEnd : other.rowEnd, file);
	}
	
	public String toString() {
		return lineStart + ":" + rowStart + " " + file;
	}
	
	public String toStringLength() {
		return toString() + " ("+(rowEnd - rowStart)+" chars)";
	}
	
	public String arrowify(String text) {
		String[] lines = text.split("\n");
		
		int startLine = 0;
		int endLine = lines.length;
		if(endLine - startLine > 8) {
			startLine = lineStart-4;
			endLine = lineEnd+4;
		}
		
		boolean singleLine = isSingleline();
		
		String str = "";
		for(int i = Math.max(startLine, 0); i < Math.min(endLine, lines.length); i++) {
			String line = lines[i];
			
			str += line + "\n";
			if(singleLine) {
				if(i == lineStart-1)
					str += "^".repeat(rowEnd - rowStart).indent(rowStart -1);
			} else if(i == lineStart-1)
				str += "^".repeat(line.length() - rowStart).indent(rowStart - 1);
			else if(i == lineEnd-1)
				str += "^".repeat(rowEnd);
			else if(i > lineStart && i < lineEnd)
				str += "^".repeat(rowEnd);
		}
		
		return str;
	}
	
	public boolean isSingleline() { return lineStart == lineEnd; }
	
}
