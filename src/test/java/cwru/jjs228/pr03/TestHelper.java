package cwru.jjs228.pr03;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Helps with test methods
 * @author joseph satterfield james wright
 *
 */
public class TestHelper {
	
	public static void fileCompareTest(String[] args, String inputFileName,
			String outputFileName) {
		try {
			FileInputStream str = new FileInputStream(inputFileName);
			System.setIn(str);
			fileCompareTest(args, outputFileName);
		} catch (IOException e) {
			fail("Unable to read from input or output file.");
		}
	}
	
	public static void fileCompareTest(String[] args,
			String outputFileName) {
		try {
			ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			System.setOut(new PrintStream(outContent));
			Main.main(args);
			String returnedContent = outContent.toString("UTF-8").trim();
			
			Scanner fileAnswerScanner = new Scanner(new BufferedReader(
					new FileReader(outputFileName)));
			Scanner providedAnswerScanner = new Scanner(returnedContent);
			while(fileAnswerScanner.hasNextLine() && providedAnswerScanner.hasNextLine()){
				String provided = providedAnswerScanner.nextLine();
				System.out.println(provided);
				assertEquals(fileAnswerScanner.nextLine(),provided);
			}
			
		} catch (IOException e) {
			fail("Unable to read from input or output file.");
		}
	}
}
