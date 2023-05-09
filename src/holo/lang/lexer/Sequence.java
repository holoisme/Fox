package holo.lang.lexer;

//public record Sequence(int line, int row, int size, String file) {
public record Sequence(int lineStart, int lineEnd, int rowStart, int rowEnd, String file) {
	
	public static Sequence from(int line, int row, int size, String file) {
		return new Sequence(line, line, row, row + size, file);
	}
	
	public Sequence join(Sequence other) {
		int lStart = Math.min(lineStart, other.lineStart);
		int lEnd = Math.max(lineEnd, other.lineEnd);
		
		return new Sequence(lStart, lEnd,
				lineStart == lStart ? rowStart : other.rowStart,
				lineEnd == lEnd ? rowEnd : other.rowEnd, file);
	}
	
	public String toString() {
		return lineStart + ":" + rowStart + " " + file;
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
