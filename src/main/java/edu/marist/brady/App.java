package edu.marist.brady;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

/**
 * Main app class.
 */
public final class App {
    /** App class constructor. */
    protected App() {
    }

    /**
     * Says hello to the world!
     * @param args The arguments of the program.
     * @throws FileNotFoundException
     */
    public static void main(final String[] args) throws FileNotFoundException {
        int returnCode = 0;
        Utils utils = new Utils();
        utils.processArgs(args);

        NFA nfa = null;
        DFA dfa = null;

        System.out.println();
        System.out.println("Welcome to Grepy 1.0!");
        System.out.print("Please enter your file path: ");
        //Scanner in = new Scanner(System.in);
        //File myFile = new File(in.nextLine());

        File myFile = new File("/Users/shannonbrady/Desktop/CMPT440-grepy/filename.txt");

        //read input file
        RegexReader reader = new RegexReader();
        reader.read(myFile);

        //generate NFA based on first line of input file
        nfa = reader.createNFA();

        //convert NFA to DFA
        dfa = reader.createDFA(nfa);

        //validate strings in file
        Scanner scanner = new Scanner(myFile);
        scanner.nextLine();
        // while (scanner.hasNextLine()) {
        //     String line = scanner.nextLine();
        //     reader.validate(dfa, line);
        // }

        //in.close();
        System.exit(returnCode);
    }
}
