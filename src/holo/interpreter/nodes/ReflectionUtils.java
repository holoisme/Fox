package holo.interpreter.nodes;

public class ReflectionUtils {
	
	public static <T> String toString(T[] array, String sep) {
		if(array.length == 0) return "";
		
		String str = "";
		for(int i = 0; i < array.length; i++)
			str += array[i].toString() + (i == array.length-1 ? "":sep);
		return str;
	}
	
	public static <T> String toString(T[] array) {
		return toString(array, ", ");
	}
	
}
