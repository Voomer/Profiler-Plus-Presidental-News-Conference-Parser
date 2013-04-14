/*
 * if anyone has feedback for the design of Context, it would be much appreciated.
 * i struggled with the design of Context, and in the end, since i felt it was
 * more of a data structure than an object, it was okay to expose the innards to the states.
 * context contains the lines from a from a file as a string array. It is the context
 * all the states use.
 */

package parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;


public class Context {
	
	Pattern record, ignore;
	State state;
	int cursor;
	String[] lines;
	int length;
	public Context(LinkedList<String> record, LinkedList<String> ignore, File file) throws IOException {
		this(record, ignore, fileToArray(file));	
	}
	
	public static String[] fileToArray(File file) throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		LineIterator lt = FileUtils.lineIterator(file);
		String current;
		while (lt.hasNext()) {
			current = lt.nextLine().trim();
			if (!current.equals(""))
				lines.add(current);
		}
		String[] array = new String[lines.size()];
		array =  lines.toArray(array);
		return array;
	}
	
	public Context(LinkedList<String> record, LinkedList<String> ignore, String[] lines) {
		StringBuilder recordRegex = new StringBuilder();
		for (String string : record) {
			recordRegex.append("^");
			recordRegex.append(string);
			recordRegex.append("|");
		}
		System.out.println(recordRegex);
		recordRegex.deleteCharAt(recordRegex.length()-1);
		System.out.println(recordRegex);
		
		StringBuilder ignoreRegex = new StringBuilder();
		for (String string : ignore) {
			ignoreRegex.append("^");
			ignoreRegex.append(string);
			ignoreRegex.append("|");
		}
		System.out.println(ignoreRegex);
		ignoreRegex.deleteCharAt(ignoreRegex.length()-1);	
		System.out.println(ignoreRegex);

		this.record = Pattern.compile(recordRegex.toString());
		this.ignore = Pattern.compile(ignoreRegex.toString());
		this.lines = lines;
		this.length = lines.length;
		System.out.println(Arrays.toString(lines));
	}
	
	public String containsAny(String string, Pattern pattern) {
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) 
			return matcher.group();			
		else
			return null;	
	}
	
	public boolean EOF() {
		return (cursor == length);
	}
	
	public String[] parse(State intial) {
		state = intial;
		while (state.go()) {}
		return lines;
	}
}
