package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import parsers.Context;
import parsers.PreProcessState;
import parsers.PresRecordState;
import parsers.PresidentSeekState;
import junit.framework.*;
import org.apache.commons.io.*;
import org.junit.Before;

import parsers.State;


public class PresidentParserTest extends TestCase {
	

	public void testRemoveEnclosings() {
		Context context = null;
		State state = new PreProcessState(context);
		String testString1 = "efwef[dewef]--";
		String resultString1 = "efwef<ignore>[dewef]</ignore>--";
		String testString2 = "Hey guys --[Hi]";
		String resultString2 = "Hey guys --<ignore>[Hi]</ignore>";
		String testString3 = "Hey guys --(Hi)";
		String resultString3 = "Hey guys --<ignore>(Hi)</ignore>";
		assertEquals(state.removeEnclosings(testString1), resultString1);
		assertEquals(state.removeEnclosings(testString2), resultString2);
		assertEquals(state.removeEnclosings(testString3), resultString3);
	}

	public void testStateSwitch() {

		LinkedList<String> record = new LinkedList<String>();
		LinkedList<String> ignore = new LinkedList<String>();
		record.add("The President.");
		record.add("THE PRESIDENT.");
		ignore.add("Q.");
		
		String[] lines = new String[3];
		lines[0] = "And now for the question.";
		lines[1] = "Q. President Ron Paul, are you happy with your accomplishments " +
				"of your adminstraton?";
		lines[2] = "The President. Yes, I am happy we managed to abolish federal " +
				"governement and turned states into independent countries";
		Context switchContext = new Context(record, ignore, lines);
		String[] results = switchContext.parse(new PresRecordState(switchContext));
		String[] expectedResults = new String[2];
		expectedResults[0] = "<ignore>Q. President Ron Paul, are you happy with your accomplishments " +
				"of your adminstraton?";
		expectedResults[1] = "The President.</ignore> Yes, I am happy we managed to abolish federal " +
				"governement and turned states into independent countries";
		assertEquals(results[1], expectedResults[0]);
		assertEquals(results[2], expectedResults[1]);

		//side effects cause this to change, maybe copy over the array?
		
		lines[0] = "Q. President Ron Paul, are you happy with your accomplishments " +
				"of your adminstraton?";
		lines[1] = "The President. Yes, I am happy we managed to abolish federal " +
				"governement and turned states into independent countries";
		Context switchContext2 = new Context(record, ignore, lines);
		String[] results2 = switchContext2.parse(new PresidentSeekState(switchContext2));
		String[] expectedResults2 = new String[2];
		expectedResults2[0] = "Q. President Ron Paul, are you happy with your accomplishments " +
				"of your adminstraton?";
		expectedResults2[1] = "The President.</ignore> Yes, I am happy we managed to abolish federal " +
				"governement and turned states into independent countries";

		assertEquals(results2[0], expectedResults2[0]);
		assertEquals(results2[1], expectedResults2[1]);

		//side effects cause this to change, maybe copy over the array?
		lines = new String[2];
		lines[0] = "PlaceHolder.";
		lines[1] = "Q. President Ron Paul, are you happy with your accomplishments " +
				"of your adminstraton?";
		Context switchContext3 = new Context(record, ignore, lines);
		String[] results3 = switchContext3.parse(new PresRecordState(switchContext3));
		String[] expectedResults4 = new String[1];
		expectedResults4[0] = "<ignore>Q. President Ron Paul, are you happy with your accomplishments " +
				"of your adminstraton?</ignore>";

		assertEquals(results3[1], expectedResults4[0]);




	}
}
