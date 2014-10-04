package cwru.jjs228.pr03;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

class Main {

	public static void main(String[] argv) throws IOException {
		ANTLRInputStream stream = new ANTLRInputStream(new FileReader(
				"input.txt"));
		arrowlangLexer lexer = new arrowlangLexer(stream);
		lexer.addErrorListener(new ExceptionErrorListener());
		
		 CommonTokenStream tokens = new CommonTokenStream(lexer);
		 arrowlangParser parser = new arrowlangParser(tokens);
		 parser.addErrorListener(new ExceptionErrorListener());
		 parser.start();
	}

	public static void Usage(Options options) {
		new HelpFormatter().printHelp("pr03", options, true);
		System.exit(1);
	}
	
	private static String getNameForTokenNumber(arrowlangLexer lexer, int number){
		return lexer.ruleNames[number-1];
	}
	
	private static void printAllTokens(arrowlangLexer lexer){
		List<? extends Token> toks = lexer.getAllTokens();
		for (Token t : toks) {
			if (t.getChannel() == 0) {
				System.out.println(getNameForTokenNumber(lexer, t.getType())+ " " + t.getText());
			}
		}
	}
}
