package test;

import java.io.File;
import java.io.IOException;

import presidentparser.PresidentParser;
import junit.framework.TestCase;
import org.apache.commons.io.*;

public class PresidentParserTest extends TestCase {
	public void testParser() throws IOException {
		PresidentParser p = new PresidentParser();
		File unparsed = new File("bush.txt");
		File output = new File("bush.pro");
		String testData = FileUtils.readFileToString(new File("Bush 1989 6-July (Q3).pro"));
		String outputText = FileUtils.readFileToString(output);
			p.parseFile(unparsed, output);	
		assertEquals(testData, outputText);
	}
}
