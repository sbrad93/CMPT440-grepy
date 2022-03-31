package edu.marist.brady;

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

        //read input file
        RegexReader reader = new RegexReader();
        reader.read();

        //generate NFA based on first line of input file
        reader.createNFA();

        System.exit(returnCode);
    }
}
