package edu.marist.brady;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

/**
 * RegexReader class.
 */
public final class RegexReader {

    public static int stateCount = 0;
    private static File myFile;
    private static Stack<NFA> nfaStack = new Stack<NFA>();
    private static Stack<Character> operators = new Stack<Character>();
    private String myRegex;
    private HashSet<Character> myAlphabet;

    /**
     * RegexReader constructor.
     */
    public RegexReader() {
        this.myRegex = "";
        this.myAlphabet = new HashSet<Character>();
    } //RegexReader constructor

    /**
     * reads input file and learns alphabet of language.
     */
    public void read(File path) {

        try {
            myFile = path;
            Scanner myReader = new Scanner(path);
            myRegex = myReader.nextLine();
            char[] regexChars = getChars(myRegex);

            // loops through characters to build alphabet
            for (int i = 0; i < myRegex.length(); i++) {
                if ((regexChars[i] != '(')
                    && (regexChars[i] != ')')
                        && (regexChars[i] != '+')
                        && (regexChars[i] != '*')
                        && (regexChars[i] != 'e')) {
                    myAlphabet.add(regexChars[i]);
                }
            }

            if (myAlphabet.size() > 2) {
                System.out.println("This program only processes two input symbols. Please try again.");
                System.exit(0);
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Please try again.");
            e.printStackTrace();
        }

        myRegex = this.addConcat(myRegex);
        myRegex = orderRegex(myRegex);
        myRegex = this.addConcat(myRegex);
    } //read

    //returns a set of all non-operator symbols in language
    private char[] getChars(String str) {
        char[] chars = new char[str.length()];

        for (int i = 0; i < str.length(); i++) {
            chars[i] = str.charAt(i);
        }

        return chars;
    } //getChars

    //returns boolean indicating whether given character is part of language
    private boolean isAlphabet(char charIn) {
        boolean ans = false;

        for (int i = 0; i < myAlphabet.size(); i++) {
            if (myAlphabet.contains(charIn)) {
                ans = true;
            }
        }

        return ans;
    } //isAlphabet

    //outputs NFA in string format
    private void printNFA(NFA nfa) {
        for (int j = 0; j < nfa.getNFA().size(); j++) {
            System.out.println(nfa.getNFA().get(j).transitionString());
        }
    }

    //outputs DFA in string format
    private void printDFA(DFA dfa) {
        for (int j = 0; j < dfa.getDFA().size(); j++) {
            System.out.println(dfa.getDFA().get(j).transitionString());
        }
    }

    //inserts "." symbol wherever concatenation is necessary
    private String addConcat(String regex) {
        String res = "";

        for (int i = 0; i < regex.length() - 1; i++) {
            if (isAlphabet(regex.charAt(i)) && isAlphabet(regex.charAt(i + 1))) {
                res += regex.charAt(i) + ".";
            } else if (isAlphabet(regex.charAt(i)) && regex.charAt(i + 1) == '(') {
                res += regex.charAt(i) + ".";
            } else if (regex.charAt(i) == ')' && isAlphabet(regex.charAt(i + 1))) {
                res += regex.charAt(i) + ".";
            } else if (regex.charAt(i) == ')' && regex.charAt(i + 1) == '(') {
                res += regex.charAt(i) + ".";
            } else if (regex.charAt(i) == '*' && isAlphabet(regex.charAt(i + 1))) {
                res += regex.charAt(i) + ".";
            } else if (regex.charAt(i) == '*' && regex.charAt(i + 1) == '(') {
                res += regex.charAt(i) + ".";
            } else {
                res += regex.charAt(i);
            }
        }

        res += regex.charAt(regex.length() - 1) + "";

        return res;
    } //addConcat

    /**
     * reorders regex string to match operator precedence.
     */
    public String orderRegex(String regex) {
        String kleeneString = "";
        String unionString = "";
        String symbolString = "";
        String res = "";
        String[] regexParts = regex.split("\\.");

        for (int i = 0; i < regexParts.length; i++) {
            if (regexParts[i].contains("*")) {
                kleeneString += regexParts[i];
            } else if (regexParts[i].contains("+")) {
                unionString += regexParts[i];
            } else {
                symbolString += regexParts[i];
            }
        }

        res = kleeneString + symbolString + unionString;

        return res;
    }

    /**
     * creates nfa based on regular expression.
     */
    public NFA createNFA() {

        NFA finalNFA = null;

        //add concatenation to inital regex
        String regex = addConcat(myRegex);

        //indicator for computing concatenation
        int addedSymbols = 0;

        //clear da stacks
        nfaStack.clear();
        operators.clear();

        for (int i = 0; i < regex.length(); i++) {

            if (isAlphabet(regex.charAt(i)) && addedSymbols < 2) { //add input symbols
                addSymbol(regex.charAt(i));
                addedSymbols++;
            } else if (regex.charAt(i) == '+' || regex.charAt(i) == '.') { //concatenation or union
                operators.add(regex.charAt(i));
            } else if (regex.charAt(i) == '*') { //kleene star
                operators.add(regex.charAt(i));
                compute();
            }

            if (addedSymbols == 2 && (operators.contains('.') || operators.contains('+'))) {
                addedSymbols--;
                compute();
            }
        }

        finalNFA = nfaStack.pop();

        //set accept state
        finalNFA.getNFA().get(finalNFA.getNFA().size() - 1).getEndState().setAcceptState(true);

        //printNFA(nfa);

        //create NFA DOT output
        Digraph nfaOut = new Digraph("myNFA");
        nfaOut.output(finalNFA);

        return finalNFA;

    } //createNFA

    //adds nfa transition for alphabet symbols
    private void addSymbol(char sym) {
        State start = new State(stateCount);
        State end = new State(stateCount);
        NFA tempNFA = new NFA();

        //create symbol transition from start state to end state
        Transition t1 = new Transition(sym, start, end);

        //add new transition
        tempNFA.getNFA().add(t1);

        //push tempNFA to nfa stack
        nfaStack.push(tempNFA);
    } //addSymbol

    //switch statement based on regex operation
    private void compute() {
        if (RegexReader.operators.size() > 0) {

            char charAt = operators.pop();

            switch (charAt) {
                case '*':
                    kleene();
                    break;

                case '+':
                    union();
                    break;

                case '.':
                    concat();
                    break;

                default:
                    System.exit(1);
                    break;
            }
        }
    } //compute

    //union (+) operation in Thompson's algorithm
    private void union() {
        NFA tempNFA2 = nfaStack.pop();
        NFA tempNFA1 = nfaStack.pop();

        //at this point there are only two symbols added (ex: a+b --> only four states, 2 transitions)
        //another symbol must be added for the middle portion of final nfa (ex: a+b ---> another a transition needed)

        //add additional symbol
        addSymbol(tempNFA1.getNFA().getLast().getTransitionSymbol());

        //create temporary nfa with this new transtion
        NFA tempNFA3 = nfaStack.pop();

        //new transition needs to be placed between intial two
        //start/end states need to switch for tempNFA3 and tempNFA2

        //local variables for start/end states for better readability
        int temp1Start = tempNFA2.getNFA().getLast().getStartState().getStateID();
        int temp3Start = tempNFA3.getNFA().getLast().getStartState().getStateID();
        int temp1End = tempNFA2.getNFA().getLast().getEndState().getStateID();
        int temp3End = tempNFA3.getNFA().getLast().getEndState().getStateID();

        //switch start states
        tempNFA2.getNFA().getLast().getStartState().setStateID(temp1Start + 2);
        tempNFA3.getNFA().getLast().getStartState().setStateID(temp3Start - 2);

        //switch end states
        tempNFA2.getNFA().getLast().getEndState().setStateID(temp1End + 2);
        tempNFA3.getNFA().getLast().getEndState().setStateID(temp3End - 2);

        //create epsilon transitions
        Transition t1 = new Transition('e',
            tempNFA1.getNFA().getLast().getEndState(),
            tempNFA3.getNFA().getFirst().getStartState());
        Transition t2 = new Transition('e',
            tempNFA1.getNFA().getLast().getEndState(),
            tempNFA2.getNFA().getFirst().getStartState());
        Transition t3 = new Transition('e',
            tempNFA3.getNFA().getLast().getEndState(),
            tempNFA3.getNFA().getFirst().getStartState());
        Transition t4 = new Transition('e',
            tempNFA3.getNFA().getLast().getEndState(),
            tempNFA2.getNFA().getFirst().getStartState());

        //add all transitions to tempNFA1
        tempNFA1.getNFA().add(t1);

        for (int i = 0; i < tempNFA3.getNFA().size(); i++) {
            tempNFA1.getNFA().add(tempNFA3.getNFA().get(i).getTransition());
        }

        tempNFA1.getNFA().add(t3);
        tempNFA1.getNFA().add(t2);
        tempNFA1.getNFA().add(t4);

        for (int i = 0; i < tempNFA2.getNFA().size(); i++) {
            tempNFA1.getNFA().add(tempNFA2.getNFA().get(i).getTransition());
        }

        //add NFA back into nfa stack
        nfaStack.push(tempNFA1);
    } //union

    //concatentation (.) operation in Thompson's algorithm
    private void concat() {

        NFA tempNFA2 = nfaStack.pop();
        NFA tempNFA1 = nfaStack.pop();

        //create epsilon transition from tempNFA1 to tempNFA2
        Transition t1 = new Transition('e',
            tempNFA1.getNFA().getLast().getEndState(),
            tempNFA2.getNFA().getFirst().getStartState());

        //add new epsilon transition to tempNFA1
        tempNFA1.getNFA().add(t1);

        //combine states into one NFA
        for (int i = 0; i < tempNFA2.getNFA().size(); i++) {
            tempNFA1.getNFA().add(tempNFA2.getNFA().get(i).getTransition());
        }

        //add NFA back into stack
        nfaStack.push(tempNFA1);
    } //concatenation

    //kleene star (*) operation in Thompson's algorithm
    private void kleene() {

        //epsilon transitions are added before and after symbol transition
        //state ID's are updated accordingly

        NFA tempNFA = nfaStack.pop();
        NFA finalNFA = new NFA();

        //remove previous transition
        Transition prev = tempNFA.getNFA().removeLast();

        //previous transition start/end states
        int prevStartID = prev.getStartState().getStateID();
        int prevEndID = prev.getEndState().getStateID();

        //create new start/end states
        State start = new State(prevStartID);
        State next = new State(prevEndID + 2);

        //change end state state pointer to new 'start' state
        if (tempNFA.getNFA().size() > 1) {
            tempNFA.getNFA().get(tempNFA.getNFA().size() - 1).setEndState(start.getState());
        }

        //update prev state IDs
        prev.getStartState().setStateID(prevStartID + 1);
        prev.getEndState().setStateID(prevEndID + 1);

        // //create epsilon transitions
        Transition t1 = new Transition('e', start, prev.getStartState());
        Transition t2 = new Transition('e', start, next);
        Transition t3 = new Transition('e', prev.getEndState(), prev.getStartState());
        Transition t4 = new Transition('e', prev.getEndState(), next);

        //add all transitions from tempNFA to finalNFA
        if (tempNFA.getNFA().size() > 1) {
            for (int i = 0; i < tempNFA.getNFA().size(); i++) {
                finalNFA.getNFA().add(tempNFA.getNFA().get(i));
            }
        }

        //add new transitions and prev
        finalNFA.getNFA().add(t1);
        finalNFA.getNFA().add(prev);
        finalNFA.getNFA().add(t3);
        finalNFA.getNFA().add(t2);
        finalNFA.getNFA().add(t4);

        //add new nfa to stack
        nfaStack.push(finalNFA);
    } //kleene

    private boolean isOnlyKleene(String regex) {
        boolean ans = false;
        int numUnion = 0;
        int numChars = 0;
        char[] chars = getChars(regex);

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '+') {
                numUnion++;
            } else if (i != chars.length - 1
                && isAlphabet(chars[i])
                && isAlphabet(chars[i + 1])) {
                numChars++;
            } else if (isAlphabet(chars[chars.length - 1])) {
                numChars++;
            }
        }

        if (numUnion == 0 && numChars == 0) {
            ans = true;
        }

        return ans;
    } //isOnlyKleene

    /**
     * converts nfa to dfa.
     */
    public DFA createDFA(NFA nfa) {
        DFA dfa = new DFA();

        //mark reverse epsilon transitions accordingly
        for (int i = 0; i < nfa.getNFA().size(); i++) {
            if (i != 0
                && nfa.getNFA().get(i).getEndState().getStateID()
                < nfa.getNFA().get(i).getStartState().getStateID()) {

                nfa.getNFA().get(i).setTransitionSymbol('%');
            }
        }

        //add all non-epsilon transitions to dfa
        //convert marked transitions
        for (int i = 0; i < nfa.getNFA().size(); i++) {
            if (nfa.getNFA().get(i).getTransitionSymbol() != 'e') {
                if (i != 0 && nfa.getNFA().get(i).getTransitionSymbol() == '%') {
                    nfa.getNFA().get(i - 1).getEndState().setStateID(nfa.getNFA().get(i).getEndState().getStateID());
                } else {
                    dfa.getDFA().add(nfa.getNFA().get(i));
                }

            }
        }

        //connect end states to the correct start state as needed
        for (int i = 0; i < dfa.getDFA().size(); i++) {
            if (i != 0 && dfa.getDFA().get(i).getStartState().getStateID()
                != dfa.getDFA().get(i - 1).getEndState().getStateID()) {

                if (dfa.getDFA().get(i - 1).getStartState().getStateID()
                    < dfa.getDFA().get(i - 1).getEndState().getStateID()
                    && dfa.getDFA().get(i).getStartState().getStateID()
                    != dfa.getDFA().get(i).getEndState().getStateID()) {

                    dfa.getDFA().get(i).setStartState(dfa.getDFA().get(i - 1).getEndState());

                } else if (dfa.getDFA().get(i - 1).getStartState().getStateID()
                    == dfa.getDFA().get(i - 1).getEndState().getStateID()
                    && dfa.getDFA().get(i).getStartState().getStateID()
                    != dfa.getDFA().get(i).getEndState().getStateID()) {

                    dfa.getDFA().get(i).setStartState(dfa.getDFA().get(i - 1).getStartState());
                }
            }
            //mark repeating states
            if (dfa.getDFA().get(i).getEndState().getStateID() == dfa.getDFA().get(i).getStartState().getStateID()) {
                dfa.getDFA().get(i).canSkip = true;
                dfa.getDFA().get(i).isRepeating = true;
            }
        }

        //connect kleene star operations if necessary
        for (int i = 0; i < dfa.getDFA().size(); i++) {
            if (i != 0 && dfa.getDFA().get(i).getStartState().getStateID()
                == dfa.getDFA().get(i).getEndState().getStateID()
                && dfa.getDFA().get(i - 1).getStartState().getStateID()
                == dfa.getDFA().get(i - 1).getEndState().getStateID()) {

                Transition t = new Transition(dfa.getDFA().get(i).getTransitionSymbol(),
                    dfa.getDFA().get(i - 1).getEndState(), dfa.getDFA().get(i).getStartState());
                //addtional transitions to connect kleene star operations added to end of dfa
                t.canSkip = true;
                t.isFiller = true;
                dfa.getDFA().add(t);
            }
        }

        //add accept states if myRegex is only kleene operations
        for (int i = 0; i < dfa.getDFA().size(); i++) {
            if (isOnlyKleene(myRegex)
                && dfa.getDFA().get(i).getStartState().getStateID() == dfa.getDFA().get(i).getEndState().getStateID()) {
                dfa.getDFA().get(i).getEndState().setAcceptState(true);
            }
        }

        // printDFA(dfa);

        //create DFA DOT output
        Digraph dfaOut = new Digraph("myDFA");
        dfaOut.output(dfa);

        return dfa;

    } //createDFA

    /**
     * checks if string is accepted by dfa.
     * loops through dfa and compares to a given string character.
     */
    public boolean validate(DFA dfa, String s) {
        Transition t = null;
        Transition next = null;
        boolean isMatch = true;

        //dfa index
        int i = 0;

        //string index
        int j = 0;

        while (i != dfa.getDFA().size() && j != s.length()) {

            t = dfa.getDFA().get(i);

            if (i != dfa.getDFA().size() - 1) {
                next = dfa.getDFA().get(i + 1);
            }

            // System.out.println(i);
            // System.out.println(j);
            // System.out.println(t.getTransitionSymbol());
            // System.out.println(s.charAt(j));
            // System.out.println();

            //conditions for a match
            if (t.getTransitionSymbol() == s.charAt(j)) {
                if (i < dfa.getDFA().size() - 1 && j < s.length() - 1 && next.canSkip && t.isRepeating) {
                    i--;
                } else if (t.isRepeating && next.getTransitionSymbol() != s.charAt(j + 1)) {
                    i--;
                }
            } else if (!isAlphabet(s.charAt(j))) { //identify characters not part of alphabet
                isMatch = false;
                break;
            } else if (t.canSkip) {  //skip unecessary transitions
                // System.out.println("can skip");
                i++;
                continue;
            } else {
                // System.out.println("not match");
                isMatch = false;
                break;
            }

            //increment indexes
            i++;
            j++;
        }

        //post-loop conditions
        //if entire dfa isn't traversed
        if (next != null) {
            if (!isOnlyKleene(myRegex) && i < dfa.getDFA().size() && !next.canSkip) {
                // System.out.println("dfa not traversed");
                isMatch = false;
            }
        }
        //if dfa is travered but string has remaining characters
        if (i == dfa.getDFA().size() && j != s.length()) {
            // System.out.println("string not traversed");
            isMatch = false;
        } else if (!isOnlyKleene(myRegex) && "".equals(s)) { //empty string
            // System.out.println("not only kleen emypty string");
            isMatch = false;
        }

        //output results
        // if (isOnlyKleene(myRegex) && isMatch && s.length() == 0) {
        //     System.out.println("true");
        // } else if (isMatch && t != null && (t.getEndState().getAcceptState() | t.canSkip)) {
        //     System.out.println("true");
        // } else if (!isMatch) {
        //     System.out.println("false");
        // }

        return isMatch;
    } //validate
} //RegexReader
