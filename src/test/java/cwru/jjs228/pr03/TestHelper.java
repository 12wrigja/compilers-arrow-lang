package cwru.jjs228.pr03;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Helps with test methods
 * 
 * @author joseph satterfield james wright
 *
 */
public class TestHelper {

	public static void fileCompareTest(String[] args,
			String outputFileName, String correctFileName) {
		try {
			Main.main(args);
			
			Scanner fileAnswerScanner = new Scanner(new BufferedReader(
					new FileReader(outputFileName)));
			Scanner providedAnswerScanner = new Scanner(new BufferedReader(new FileReader(correctFileName)));
			while (fileAnswerScanner.hasNextLine()
					|| providedAnswerScanner.hasNextLine()) {
				String provided = providedAnswerScanner.nextLine();
				String computed = fileAnswerScanner.nextLine();
				assertEquals(computed, provided);
			}
			providedAnswerScanner.close();
			fileAnswerScanner.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unable to read from input or output file.");
		}
	}
}
