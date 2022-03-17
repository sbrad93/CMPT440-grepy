package edu.marist.brady;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

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
     */
    public static void main(final String[] args) {
        int returnCode = 0;
        Utils utils = new Utils();
        utils.processArgs(args);

        Scanner inObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter regular expression: ");

        String regex = inObj.nextLine();  // Read user input
        char[] regexChars = new char[regex.length()];
		HashSet<Character> alphabet = new HashSet<Character>();

		for (int i= 0; i<regex.length(); i++) {
		    regexChars[i] = regex.charAt(i);
		}
		
		for (int i= 0; i<regex.length(); i++) {
			if ((regexChars[i] != '(') &&
					(regexChars[i] != ')') &&
					(regexChars[i] != '+') && 
					(regexChars[i] != '*')) {
				alphabet.add(regexChars[i]);
			}
		}
		
		System.out.println(alphabet);

        try {
            File myObj = new File("/Users/shannonbrady/Desktop/CMPT440-grepy/filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Hello");

        System.exit(returnCode);
    }
}
