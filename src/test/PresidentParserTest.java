package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import parsers.Context;
import parsers.PreProcessState;
import junit.framework.*;
import org.apache.commons.io.*;
import org.junit.Before;

import Deprecated.PresidentParser;
import Deprecated.PresidentParser.Result;

public class PresidentParserTest extends TestCase {
	
	public void testParser() throws IOException {
		HashSet<String> record = new HashSet<String>();
		HashSet<String> ignore = new HashSet<String>();
		record.add("The President.");
		record.add("THE PRESIDENT.");
		ignore.add("Q.");
		File unparsed = new File("bush.txt");
		File output = new File("bush.pro");
		String[] testData = Context.fileToArray(new File("Bush 1989 6-July (Q3).pro"));
		String outputText = FileUtils.readFileToString(output);
		Context context = new Context(record, ignore, unparsed);
		String[] result = context.parse(new PreProcessState(context));
		System.out.println(result.toString());
		FileUtils.writeLines(output, Arrays.asList(result));
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
}
