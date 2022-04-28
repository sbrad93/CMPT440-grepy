package edu.marist.brady;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main app class.
 */
public final class App {
    /** App class constructor. */
    protected App() {
    }

    /**
     * Grep for regular expressions.
     * @param args The arguments of the program.
     * @throws FileNotFoundException
     */
    public static void main(final String[] args) throws FileNotFoundException {
        int returnCode = 0;
        Utils utils = new Utils();
        utils.processArgs(args);

        NFA nfa = null;
        DFA dfa = null;
        boolean res = false;

        System.out.println();
        System.out.println("Welcome to Grepy 1.0!");
        System.out.print("Please enter your file path: ");
        Scanner in = new Scanner(System.in);

        //test input files located in test_inputs folder
        File myFile = new File(in.nextLine());

        System.out.println("\n--------------------------------------------------");

        //read input file
        RegexReader reader = new RegexReader();
        reader.read(myFile);

        //generate NFA based on first line of input file
        nfa = reader.createNFA();

        //convert NFA to DFA
        dfa = reader.createDFA(nfa);

        try {
            Scanner scanner = new Scanner(myFile);
            FileWriter writer = new FileWriter("accepted_strings.txt");

            //ignore first line (contains regex expression)
            String regex = scanner.nextLine();

            System.out.println("\nAccepted Strings:");
            writer.write("Accepted Strings for " + regex + "\n\n");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //validate strings in file
                res = reader.validate(dfa, line);

                //write accepted strings to output file
                if (res) {
                    if ("".equals(line)) {
                        line = "{empty string}";
                    }
                    System.out.println(line);
                    writer.write(line + "\n");
                }
            }

            System.out.println("\nCheck out the following files for your results:");
            System.out.println("nfa.txt");
            System.out.println("dfa.txt");
            System.out.println("accepted_strings.txt\n");
            System.out.println("Have a great day!\n");

            writer.close();
            scanner.close();
        } catch (IOException e) {
            System.out.println("Something blew up. Please try again.");
            e.printStackTrace();
        }

        in.close();
        System.exit(returnCode);
    }
}
