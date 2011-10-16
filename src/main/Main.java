package main;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.*;
import org.apache.commons.lang.*;
import presidentparser.*;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {

		if (args.length > 2 ) {
			System.out.println("incorrent amount of arguments");
		}
		
		else {;
			PresidentParser parser = new PresidentParser(); 
			File input = new File(args[0]);
			File output = new File(args[1]);
			if (input.isDirectory() && output.isDirectory()) {
				System.out.println("processing directory");
				File[] inputFiles = input.listFiles();
				for (int i = 0; i < inputFiles.length; i++) {
					if ( !(inputFiles[i].isHidden()) && inputFiles[i].getName().endsWith(".txt")) {
						System.out.println("processing " + inputFiles[i].getName());
						parser.parseFile(inputFiles[i], new File(output + "//" + inputFiles[i].getName().replace(".txt", ".pro")));
					}
				}
			}
			
			else if(input.isFile()) {
				System.out.println("processing file");
				parser.parseFile(new File(args[0]), new File(args[1]));
				
			}

			else {
				System.out.println("Arguments must be either both directories a file.");
			}
		}
	}
}
