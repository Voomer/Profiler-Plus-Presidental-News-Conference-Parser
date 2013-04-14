

package Deprecated;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.*;
import org.apache.commons.lang3.*;
import org.apache.commons.io.*;


public class PresidentParser {
	static String newLine = String.format("%n");;
	static Pattern presidentPattern = Pattern.compile("(?i)The President.");
	HashSet<String> ignore;
	HashSet<String> record;

	public enum State{
		PRESIDENT, OTHERSPEAKER
	}

	public PresidentParser()
	{
		ignore = new HashSet<String>();
		record = new HashSet<String>();
		ignore.add("Voices:");
		ignore.add("Q.");
		ignore.add("REPORTER.");
		ignore.add("Reporter:");
		record.add("The President.");
		record.add("The President:");
		record.add("THE PRESIDENT.");
		record.add("THE PRESIDENT:");
	}
	
	public void parseFile(File inputFile, File outputFile) {
		try {
			LineIterator lt = FileUtils.lineIterator(inputFile);
			String currentline = lt.nextLine();
			FileWriter writer = new FileWriter(outputFile);
			State state;
			//document always starts with ignored
			currentline = "<ignore>" + currentline;
			String previousline;
			//Checks to see if the interview starts off with a question, may not be necessary
			if (StringUtils.contains(currentline, "Q.") == false) {
				boolean questionFound = false;
				//looks for the first question
				while (questionFound == false) {
					writer.write(currentline + newLine);
					currentline = lt.nextLine();
					if (StringUtils.contains(currentline, "Q.")) {
						questionFound = true;
					}
				}
			}
			previousline = currentline;
			state = State.OTHERSPEAKER;
			Result result;
			while(lt.hasNext()) {
				currentline = lt.next();
				switch (state) {
				// being in this state means the president is talking
				case PRESIDENT:
					result = presidentCase(previousline, currentline);
					currentline = result.currentLine;
					previousline = result.previousLine;
					if (result.state != null)
							state = result.state;					
				case OTHERSPEAKER:
					if (currentline.startsWith("The President.") || currentline.startsWith("THE PRESIDENT.")
							|| currentline.startsWith("THE PRESIDENT:")) {	
						currentline = presidentPattern.matcher(currentline).replaceFirst("The President.</ignore>");
						state = State.PRESIDENT;
						currentline = removeBrackets(currentline);	
					}
				default: break; }
				writer.write(previousline + newLine);
				previousline = currentline;
			}
			writer.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	//removes brackets for a line
	public String removeBrackets(String currentline)
	{
		currentline = StringUtils.replace(currentline, "[", "<ignore>[");		
		currentline = StringUtils.replace(currentline, "]", "]</ignore>");
		return currentline;
	}
	
	public Result otherSpeakerCase(String currentLine) {
		if (currentLine.startsWith("The President.") || currentLine.startsWith("THE PRESIDENT.")
				|| currentLine.startsWith("THE PRESIDENT:")) {	
			currentLine = presidentPattern.matcher(currentLine).replaceFirst("The President.</ignore>");
			currentLine = removeBrackets(currentLine);	
		}
		return null;
	}
	
	public Result presidentCase(String previousLine, String currentLine) {
		State state = null;
		if (StringUtils.contains(currentLine, "Q.") ) {
			//checks to see if the previous line is a title by
			if ((previousLine.trim().length() != 0 ) && Character.isLetter
					(previousLine.charAt(previousLine.trim().length() - 1))) 
				previousLine = "<ignore> " + previousLine;
				else if (currentLine.startsWith("[")) {
					currentLine = ("<ignore>" + currentLine);
				} else {
					currentLine = StringUtils.replace(currentLine, "Q.", "<ignore>Q.");
				}
			state = State.OTHERSPEAKER;
		} else if(checkBeginning(currentLine, ignore)) {
			currentLine = ("<ignore>" + currentLine);
		} else {
			currentLine = removeBrackets(currentLine);
		}
		return new Result(state, currentLine, previousLine);
	}
	
	//this thing exists because java doesn't allow me to have multiple return types
	
	public boolean checkBeginning(String string, HashSet<String> set) {
		Iterator<String> iter = set.iterator();
		String check;
		while (iter.hasNext()) {;
			check = iter.next();
			if (string.startsWith(check)) return true;
		}
		return false;	
	}
	
	public class Result {
		public State state;
		public String currentLine;
		public String previousLine;
		public Result(State state, String currentLine,String previousLine)	{
			this.state = state;
			this.currentLine = currentLine;
			this.previousLine = previousLine;
		}
	}
}