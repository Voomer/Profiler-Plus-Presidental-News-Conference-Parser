package main;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.commons.io.*;


import parsers.*;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		if (args.length > 4 ) {
			System.out.println("incorrent amount of arguments");
		}
		
		else {
			String path = System.getProperty("user.dir");
			System.out.println(path);
			LinkedList<String> record = new LinkedList<String>(FileUtils.readLines(new File(args[2])));
			LinkedList<String> ignore = new LinkedList<String>(FileUtils.readLines(new File(args[3])));
			File input = new File(args[0]);
			File output = new File(args[1]);
			Context context;
			String[] parsed;
			if (input.isDirectory() && output.isDirectory()) {
				System.out.println("processing directory");
				File[] inputFiles = input.listFiles();
				
				for (int i = 0; i < inputFiles.length; i++) {
					if ( !(inputFiles[i].isHidden()) && inputFiles[i].getName().endsWith(".txt")) {
						System.out.println("processing " + inputFiles[i].getName());
						context = new Context(record, ignore, inputFiles[i]);
						parsed = context.parse(new PreProcessState(context));
						FileUtils.writeLines(new File(output + "//" + inputFiles[i].getName().replace(".txt", ".pro")), Arrays.asList(parsed));
					}
				}
			}
			
			else if(input.isFile()) {
				System.out.println("processing file");
				context = new Context(record, ignore, new File(args[0]));
				parsed = context.parse(new PreProcessState(context));
				FileUtils.writeLines(new File(args[1]), Arrays.asList(parsed));			
			}

			else {
				System.out.println("Arguments must be either both directories a file.");
			}					
		}	
	}
	
	public static String getPathToJarfileDir(Object classToUse) {
		  String url = classToUse.getClass().getResource("/" + classToUse.getClass().getName().replaceAll("\\.", "/") + ".class").toString();
		  url = url.substring(4).replaceFirst("/[^/]+\\.jar!.*$", "/");
		  try {
		      File dir = new File(new URL(url).toURI());
		      url = dir.getAbsolutePath();
		  } catch (MalformedURLException mue) {
		      url = null;
		  } catch (URISyntaxException ue) {
		      url = null;
		  }
		  return url;
		}
}
