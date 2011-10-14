package presidentparser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;
import org.apache.commons.lang.*;
import org.apache.commons.io.*;

public class PresidentParser {
	static String newLine = String.format("%n");;
	static Pattern presidentPattern = Pattern.compile("(?i)The President.");

	private enum State{
		PRESIDENT, OTHERSPEAKER
	}

	public PresidentParser()
	{
	}

	public void parseFile(File inputFile, File outputFile) {
		try {
			LineIterator lt = FileUtils.lineIterator(inputFile);
			String currentline = lt.nextLine();
			FileWriter writer = new FileWriter(outputFile);
			State state;
			currentline = "<ignore>" + currentline;
			String previousline;
			if (StringUtils.contains(currentline, "Q.") == false) 
			{
				boolean questionFound = false;
				while (questionFound == false)
				{
					writer.write(currentline + newLine);
					currentline = lt.nextLine();
					if (StringUtils.contains(currentline, "Q.")) {
						questionFound = true;
					}
				}
			}
			previousline = currentline;
			state = State.OTHERSPEAKER;
			while(lt.hasNext())
			{
				currentline = lt.next();
				switch (state) {
				case PRESIDENT:
					//writer.write("--------------STATE PRESIDENT--------------------" + newLine);
					//should be changed to substrings
					if (StringUtils.contains(currentline, "Q.") ) {
						if ((previousline.trim().length() != 0 ) && Character.isLetter(previousline.charAt(previousline.trim().length() - 1))) {
							previousline = "<ignore> " + previousline;
						}
						else {
							if (currentline.startsWith("["))
								currentline = ("<ignore>" + currentline);
							else
								currentline = StringUtils.replace(currentline, "Q.", "<ignore>Q.");
						
						}
						state = State.OTHERSPEAKER;
					}
					else if(currentline.startsWith("Reporter:") || currentline.startsWith("Voices:") || currentline.startsWith("REPORTER."))
						currentline = ("<ignore>" + currentline);
					else
					{
						currentline = StringUtils.replace(currentline, "[", "<ignore>[");		
						currentline = StringUtils.replace(currentline, "]", "]</ignore>");
					}
				case OTHERSPEAKER:
					//writer.write("--------------STATE QUESTION--------------------" + newLine);
					//needs to be chagnged to use the pattern
					//if (StringUtils.contains(currentline, "The President.") == true) {
					if (currentline.startsWith("The President.") || currentline.startsWith("THE PRESIDENT.")) {	
					//currentline = StringUtils.replace(currentline, "The President.", "The President.</ignore>");
						currentline = presidentPattern.matcher(currentline).replaceFirst("The President.</ignore>");
						state = State.PRESIDENT;
						//System.out.println("State Change President");
						//writer.write("Question did this---->");
						currentline = StringUtils.replace(currentline, "[", "<ignore>[");		
						currentline = StringUtils.replace(currentline, "]", "]</ignore>");
					}
				default: break;
				}
				writer.write(previousline + newLine);
				previousline = currentline;
			}
			writer.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void preProcess(LineIterator lt, FileWriter writer) {

	}




}