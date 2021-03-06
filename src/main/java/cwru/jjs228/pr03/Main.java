package cwru.jjs228.pr03;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cwru.jjs228.pr03.SymbolTable.SymbolTableException;

class Main {

	// Create Command Line Options, register them with the CommandLine and
	// GNUParser
	private final static Option helpOpt = new Option("h", "help", false,
			"Print this message");
	private final static Option outputOpt = new Option("o", true,
			"used to specify the output path");
	private final static Option ASTOption = new Option("A", "ast", false,
			"stop at AST Generation");
	private final static Option typedASTOption = new Option("T", "typed-ast",
			false, "stop at type-checked AST");
	private final static Option intermediateOption = new Option("I",
			"intermediate", false, "stop at intermediate code generation");
	private final static Option intermediateJSONOption = new Option("J",
			"json-il", false, "like -I but generate JSON");
	private final static Option assemblyOption = new Option("S", "assembly",
			false, "stop at assembly generation");
	private final static Option visualizeASTOption = new Option("V",
			"visualize", false, "prints the dot format of the AST to ST. Out.");
	private final static Option debugOption = new Option("D","Debug option. DO NOT ENABLE."); 
	
	
	private final static Options options = new Options();

	/**
	 * Main class for the compiler
	 * 
	 * @param argv
	 *            arguments for the compiler
	 * @throws IOException
	 * @throws TypeCheckingException 
	 * @throws SymbolTableException 
	 */
	public static void main(String[] argv) throws IOException, TypeCheckingException, SymbolTableException {

		options.addOption(helpOpt);
		options.addOption(outputOpt);
		options.addOption(ASTOption);
		options.addOption(typedASTOption);
		options.addOption(intermediateOption);
		options.addOption(intermediateJSONOption);
		options.addOption(assemblyOption);
		options.addOption(visualizeASTOption);
		options.addOption(debugOption);
		
		GnuParser gnuparser = new GnuParser();
		CommandLine line = null;
		try {
			line = gnuparser.parse(options, argv);
		} catch (ParseException e) {
			System.err.println("Unable to parse command line options.");
			showUsage();
		}
		Option[] opts = line.getOptions();

		if (line.hasOption(helpOpt.getLongOpt())) {
			showUsage();
		}

		String[] extraArgs = line.getArgs();
		if (extraArgs.length == 0) {
			System.err.println("Must supply some input paths.");
			System.err.println("pr03 -o <path> <input>+");
			System.err.println("Try -h or --help for help.");
			System.exit(1);
		}

		String outputFileName = null;
		if (line.hasOption(outputOpt.getOpt())) {
			outputFileName = line.getOptionValue(outputOpt.getOpt());
		} else{
			outputFileName = "output";
		}

		ANTLRInputStream stream = new ANTLRInputStream(new FileReader(
				extraArgs[0]));
		ArrowLangLexer lexer = new ArrowLangLexer(stream);
		lexer.addErrorListener(new ExceptionErrorListener());
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ArrowLangParser parser = new ArrowLangParser(tokens);
		parser.addErrorListener(new ExceptionErrorListener());
		Node<?> ast = null;
		try {
			ParseTree tree = parser.start();
			ArrowLangASTVisitor visitor = new ArrowLangASTVisitor();
			ast = visitor.visit(tree);
		} catch (Exception e) {
			if(!line.hasOption(debugOption.getOpt())){
				Helper.endExecutionWithError(e.getMessage());
			}else{
				throw e;
			}
		}
		

		if (line.hasOption(visualizeASTOption.getLongOpt())) {
			String dotFormat = dotFormat(ast);
			String treeFileName = outputFileName + "_tree";
			FileWriter writer = new FileWriter(treeFileName);
			writer.write(dotFormat);
			writer.flush();
			writer.close();
			Runtime.getRuntime().exec(
					"dot " + treeFileName + " -Tpng -o " + treeFileName
							+ ".png");
			writer.close();
		}
		
		if (line.hasOption(ASTOption.getLongOpt())) {
			String traversal = preOrderTraverse(ast, "");
			// System.out.println((traversal.isEmpty())?"No traversal data.":traversal);
			FileWriter writer = new FileWriter(outputFileName);
			writer.write(traversal);
			writer.flush();
			writer.close();
			return;
		}
		
		TypedNode<?> typedAst = new TypedNode<>(ast);
		ArrowLangTypeCheckerVisitor typeVisitor = new ArrowLangTypeCheckerVisitor();
		try {
			typeVisitor.visit(typedAst);
		} catch (TypeCheckingException e) {
			if(!line.hasOption(debugOption.getOpt())){
				Helper.endExecutionWithError(e.getMessage());
			}else{
				throw e;
			}
		} catch (SymbolTableException e) {
			if(!line.hasOption(debugOption.getOpt())){
				Helper.endExecutionWithError(e.getMessage());
			}else{
				throw e;
			}
		}
		
		if(line.hasOption(typedASTOption.getLongOpt())) {
			String traversal = preOrderTraverseType(typedAst, "");
			FileWriter writer = new FileWriter(outputFileName);
			writer.write(traversal);
			writer.flush();
			writer.close();
			return;
		}
		
		//Insert new stuff above here
		//Delete when we have a legit compiler
		Helper.endExecutionWithError("No operations past typed AST creation supported yet");
	}

	public static void Usage(Options options) {
		new HelpFormatter().printHelp("pr03", options, true);
		System.exit(1);
	}

	private static String preOrderTraverse(Node<?> node, String output) {
		if (null == node) {
			return output;
		}
		output = output.concat(node.kids.size() + ":" + node.label
				+ ((null != node.value) ? "," + node.value + "\n" : "\n"));
		List<Node<?>> kids = node.kids;
		for (Node<?> kid : kids) {
			output = preOrderTraverse(kid, output);
		}
		return output;
	}
	
	private static String preOrderTraverseType(TypedNode<?> node, String output) {
		if (null == node) {
			return output;
		}
		output = output.concat(node.kids.size() + ":" + node.label
				+ ((null != node.value) ? "," + node.value : ""))
				+ ((null != node.type) ? ":" + node.type.toString() + "\n": "\n");
		List<TypedNode<?>> kids = node.kids;
		for (TypedNode<?> kid : kids) {
			output = preOrderTraverseType(kid, output);
		}
		return output;
	}

	private static String dotFormat(Node<?> node) {
		// Name, Label
		String nodeFormat = "%s [shape=rect, label=\"%s\"];";
		String leafFormat = "%s [shape=rect, label=\"%s\", style=\"filled\" fillcolor=\"#dddddd\"];";

		//
		String edgeFormat = "%s -> %s;";

		List<String> nodes = new ArrayList<String>();
		List<String> edges = new ArrayList<String>();

		int i = 0;
		Queue<Object[]> queue = new LinkedList<Object[]>();
		queue.add(new Object[] { i, node });
		i += 1;
		while (!queue.isEmpty()) {
			Object[] item = queue.poll();
			int count = (int) item[0];
			Node<?> current = (Node<?>) item[1];
			String name = "n" + count;
			String label = (current.value != null) ? current.label + ","
					+ current.value.toString() : current.label;
			if (current.kids.size() == 0) {
				nodes.add(String.format(leafFormat, name, label));
			} else {
				nodes.add(String.format(nodeFormat, name, label));
				List<Node<?>> kids = current.kids;
				for (Node<?> kid : kids) {
					edges.add(String.format(edgeFormat, name, "n" + i));
					queue.add(new Object[] { i, kid });
					i += 1;
				}
			}
		}

		return "digraph G {\n" + join(nodes, "\n") + "\n" + join(edges, "\n")
				+ "\n}\n";
	}

	private static String join(List<?> objs, String joinTxt) {
		boolean first = true;
		StringBuffer buf = new StringBuffer();
		for (Object obj : objs) {
			if (!first) {
				buf.append(joinTxt + obj.toString());
			} else {
				buf.append(obj.toString());
				first = false;
			}
		}
		return buf.toString();
	}

	/**
	 * Prints the help for the project
	 */
	private static void showUsage() {
		new HelpFormatter().printHelp("pr03", options, true);
		System.exit(1);
	}
}
