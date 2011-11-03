package test;

import java.io.File;
import java.io.IOException;

import presidentparser.PresidentParser;
import presidentparser.PresidentParser.Result;
import junit.framework.*;
import org.apache.commons.io.*;
import org.junit.Before;

public class PresidentParserTest extends TestCase {
	
	public void testParser() throws IOException {
		PresidentParser parser = new PresidentParser();
		File unparsed = new File("bush.txt");
		File output = new File("bush.pro");
		String testData = FileUtils.readFileToString(new File("Bush 1989 6-July (Q3).pro"));
		String outputText = FileUtils.readFileToString(output);
			parser.parseFile(unparsed, output);	
		assertEquals(testData, outputText);
	}
	
	public void testReplaceBrackets() {
		PresidentParser parser = new PresidentParser();
		String testString1 = "efwef[dewef]--";
		String resultString1 = "efwef<ignore>[dewef]</ignore>--";
		String testString2 = "Hey guys --[Hi]";
		String resultString2 = "Hey guys --<ignore>[Hi]</ignore>";
		assertEquals(parser.removeBrackets(testString1), resultString1);
		assertEquals(parser.removeBrackets(testString2), resultString2);
	}
	
	public void testPresidentCase() {
		PresidentParser parser = new PresidentParser();
		String previousLine1 = "Soviet-U.S. Relations";
		String previousLine2 = "So how about them NATO guys?";
		String currentLine1 = "Q. Mr. President, despite your recent success at the NATO summit,";
		String testResult1 = "<ignore> Soviet-U.S. Relations";
		String testResult2 = "<ignore>Q. Mr. President, despite your recent success at the NATO summit,";
		
		Result result = parser.presidentCase(previousLine1, currentLine1);
		assertEquals(testResult1, result.previousLine);
		assertEquals(currentLine1, result.currentLine);
		result = parser.presidentCase(previousLine2, currentLine1);
		assertEquals(previousLine2, result.previousLine);
		assertEquals(testResult2, result.currentLine);
		result = parser.presidentCase(previousLine1, previousLine2);
		assertEquals(previousLine2, result.currentLine);
		assertEquals(previousLine1, result.previousLine);
		
	}
	
}
