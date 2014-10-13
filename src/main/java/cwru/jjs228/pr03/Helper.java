package cwru.jjs228.pr03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A class of helper functions for the project
 * @author Joseph Satterfield, James Wright
 *
 */
public class Helper {
	
	/**
	 * Outputs a line of text to standard output
	 * @param str the string to output
	 */
	public static void printToStOut(String str){
		System.out.println(str);
	}
	
	/**
	 * Outputs a line to the standard error output
	 * @param str the string to output
	 */
	public static void printToStErr(String str){
		System.err.println(str);
	}
	
	/**
	 * Ends the program and sends a error to std.err
	 * @param error the error to print to std.err
	 * @param errorCode the error code to break execution with
	 */
	public static void endExecutionWithError(String error){
		printToStErr(error);
		System.exit(1);
	}
	
	/**
	 * Quits the program with an error code, pinting errors to stderr
	 * @param errors the errors to print to stderr
	 */
	public static void endExecutionWithError(String[] errors){
		for(String error: errors){
			printToStErr(error);
		}
		System.exit(1);
	}
	
	/**
	 * Transforms characters to readable strings
	 * @param input the integer representation of the character
	 * @return the string of the character
	 */
	public static String getReadableChar(int input) {
		switch (input){
		case 10: return "\\n";
		case 9: return "\\t";
		case 32: return " ";
		default: return ""+(char)input;
		}
	}
	
	/**
	 * Transforms a string into all readable characters
	 * @param input the input string
	 * @return the transformed output string
	 */
	public static String getReadableString(String input){
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<input.length(); i++){
			buf.append(getReadableChar(input.charAt(i)));
		}
		return buf.toString();
	}
	
	/**
	 * Turns an input stream into a string a array
	 * @param is the input to read
	 * @return the input strings
	 */
	public static String[] convertStreamToStringArray(java.io.InputStream is) {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			List<String> lines = new ArrayList<String>();
			while ((line = br.readLine()) != null)
				lines.add(line);
			return lines.toArray(new String[0]);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Converts a stream to a single string
	 * @param is the input to read
	 * @return the input string
	 */
	public static String convertStreamToString(java.io.InputStream is){
		String[] parts = convertStreamToStringArray(is);
		StringBuffer buf = new StringBuffer();
		for(String s : parts){
			buf.append(s);
		}
		return buf.toString();
	}
}
