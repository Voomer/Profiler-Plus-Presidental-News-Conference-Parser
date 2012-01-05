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
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;


public class Context {
	
	HashSet<String> record, ignore;
	State state;
	int cursor;
	String[] lines;
	int size;
	public Context(HashSet<String> record, HashSet<String>ignore, File file) throws IOException {
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
	
	public Context(HashSet<String> record, HashSet<String>ignore, String[] lines) {
		this.record = record;
		this.ignore = ignore;
		this.lines = lines;	
		this.size = lines.length;
	}
	
	public String containsAny(String string, HashSet<String> set) {
		Iterator<String> iter = set.iterator();
		String check;
		while (iter.hasNext()) {;
			check = iter.next();
			if (string.contains(check)) return check;
		}
		return null;	
	}
	
	public boolean EOF() {
		return (cursor == size);
	}
	
	
	public String[] parse(State intial) {
		state = intial;
		while (state.go()) {}
		return lines;
	}

}
