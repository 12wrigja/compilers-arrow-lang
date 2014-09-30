package cwru.jjs228.pr03;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

class Main {


    public static void main(String[] argv) {

        final Option helpOpt = new Option("h", "help", false, "Print this message");
        final Option exampleArgOpt = new Option("a", "arg-example", true, "Example option which accepts and argument");
        final Options options = new Options();

        options.addOption(helpOpt);
        options.addOption(exampleArgOpt);

        String exampleArg = "";
        String[] args = null;

        try {
            GnuParser parser = new GnuParser();
            CommandLine line = parser.parse(options, argv);

            if (line.hasOption(helpOpt.getLongOpt())) {
                Usage(options);
            }

            exampleArg = line.getOptionValue(exampleArgOpt.getLongOpt());
            args = line.getArgs();

        } catch (final MissingOptionException e) {
            System.err.println(e.getMessage());
            Usage(options);
        } catch (final UnrecognizedOptionException e) {
            System.err.println(e.getMessage());
            Usage(options);
        } catch (final ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        System.out.print("extra args ");
        for (String arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
        System.out.println("arg-example = " + exampleArg);
        System.out.println("Put your logic here");
    }

    public static void Usage(Options options) {
        new HelpFormatter().printHelp("pr03", options, true);
        System.exit(1);
    }
}

