package edu.marist.brady;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public final class RegexReader {

    private String myRegex;
    private HashSet<Character> myAlphabet;
    public static int stateCount = 0;
    private static Stack<NFA> nfaStack = new Stack<NFA>();
    private static Stack<Character> operators = new Stack<Character>();

    public RegexReader() {
        this.myRegex = "";
        this.myAlphabet = new HashSet<Character>();
    }//RegexReader constructor

    //reads input file and learns alphabet of language
    public void read() {

        try {
            //need to change to user input
            File myObj = new File("/Users/shannonbrady/Desktop/CMPT440-grepy/filename.txt");
            Scanner myReader = new Scanner(myObj);

            myRegex = myReader.nextLine();
            char[] regexChars = getChars(myRegex);
            
            // loops through characters to build alphabet
            for (int i= 0; i<myRegex.length(); i++) {
                if ((regexChars[i] != '(') &&
                        (regexChars[i] != ')') &&
                        (regexChars[i] != '+') && 
                        (regexChars[i] != '*') && 
                        (regexChars[i] != 'e')) {
                    myAlphabet.add(regexChars[i]);
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }

        //testing testing testing
        System.out.println(myAlphabet);

        System.out.println(myRegex);

        myRegex = this.addConcat(myRegex);
        System.out.println(myRegex);

        myRegex = orderRegex(myRegex);
        System.out.println(myRegex);

        myRegex = this.addConcat(myRegex);
        System.out.println(myRegex);

        // String tempRegex = orderRegex(myRegex);
        // System.out.println(tempRegex);
        // System.out.println(isAlphabet('x'));
        // System.out.println(isAlphabet('a'));
        //---------------------------
    } //read

    //returns a set of all non-operator symbols in language
    private char[] getChars(String str) {
        char[] chars = new char[str.length()];
        for (int i= 0; i<str.length(); i++) {
            chars[i] = str.charAt(i);
        }
        return chars;
    }//getChars

    //returns boolean indicating whether given character is part of language
    private boolean isAlphabet(char charIn) {
        boolean ans = false;

        for (int i=0; i<myAlphabet.size(); i++) {
            if (myAlphabet.contains(charIn))
                ans = true;
        }

        return ans;
    } //isAlphabet

    //inserts "." symbol wherever concatenation is necessary
    private String addConcat(String regex) {
        String res = "";

        for(int i=0; i<regex.length()-1; i++) {
            if (isAlphabet(regex.charAt(i)) && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (isAlphabet(regex.charAt(i)) && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i) == ')' && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i) == ')' && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i) == '*' && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i) == '*' && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else res += regex.charAt(i);
            //System.out.println(regex.charAt(i));
        }
        res += regex.charAt(regex.length() - 1)+"";
        return res;
    }//addConcat

    // //indicates precedence of regex operator
    // private int precedence(char c) {
    //     if(c == '*') return 4;
    //     else if(c == '+') return 3;
    //     else if(c == '.') return 2;
    //     else return 1;
    // }//precedence

    //reorders regex string to match operator precedence
    public String orderRegex(String regex) {
        String kleeneString = "";
        String unionString = "";
        String res = "";
        String[] regexParts = regex.split("\\.");

        for (int i=0; i<regexParts.length; i++) {
            // System.out.println(regexParts.length);
            // System.out.println(regexParts[i]);
            if (regexParts[i].contains("*") )
                kleeneString += regexParts[i];
            else if (regexParts[i].contains("+"))
                unionString += regexParts[i];
            else kleeneString += regexParts[i];
        }

        res = kleeneString + unionString;
        
        return res;
    }

    //creates nfa based on regex expression
    public NFA createNFA() {

        NFA finalNFA = null;

        //add concatenation to inital regex
        String regex = addConcat(myRegex);
        int addedSymbols = 0;

        //clear da stacks
        nfaStack.clear();
        operators.clear();

        for (int i=0; i<regex.length(); i++) {

            if (isAlphabet(regex.charAt(i)) && addedSymbols < 2) {
                addSymbol(regex.charAt(i));
                addedSymbols++;
            }

            else if (regex.charAt(i) == '*') {
                operators.add(regex.charAt(i));
                compute();
            }

            else if (regex.charAt(i) == '+' || regex.charAt(i) == '.') {
                operators.add(regex.charAt(i));
            }

            if (addedSymbols == 2) {
                addedSymbols--;
                compute();
            }
        }

        finalNFA = nfaStack.pop();
        
        finalNFA.getNFA().get(finalNFA.getNFA().size()-1).endState.setAcceptState(true);

        //testing ---------------------------

        //output format in logical transition order
        for (int j=0; j< finalNFA.getNFA().size(); j++)
                System.out.println(finalNFA.getNFA().get(j).transitionString());

        //another output format using hashmap
        for (Map.Entry<Character, ArrayList<Map.Entry<State, State>>> entry : Transition.getTransitionMap().entrySet()) {
            char key = entry.getKey();
            ArrayList<Map.Entry<State, State>> value = entry.getValue();

            System.out.println("-------------");
            System.out.println("key: " + key);

            for (int i=0; i<value.size(); i++) {
                System.out.println("Start: " + value.get(i).getKey().stateID +
                    "\nEnd: " + value.get(i).getValue().stateID + "\n");
            }
        }

        //------------------------------------

        return finalNFA;
        
    }//createNFA

    //adds nfa transition for alphabet symbols
    private void addSymbol(char sym) {
        State start = new State (stateCount);
        State end = new State (stateCount);

        //create symbol transition from start state to end state
        Transition t1 = new Transition(sym, start, end);
        
        NFA tempNFA = new NFA();

        //add new transition
        tempNFA.getNFA().add(t1);

        //push tempNFA to nfa stack
        nfaStack.push(tempNFA);
    }//addSymbol

    //switch statement based on regex operation
    private void compute() {
        if(RegexReader.operators.size() > 0) {

            char charAt = operators.pop();

            switch(charAt) {
                case('*'):
                    kleene();
                    break;

                case('+'):
                    union();
                    break;

                case('.'):
                    concat();
                    break;

                default:
                    System.exit(1);
                    break;
            }
        }
    }//compute

    //union (+) operation in Thompson's algorithm
    //kinda brute force for now
    private void union() {
        NFA tempNFA2 = nfaStack.pop();
        NFA tempNFA1 = nfaStack.pop();

        //at this point there are only two symbols added (ex: a+b --> only four states, 2 transitions)
        //another symbol must be added for the middle portion of final nfa (ex: a+b ---> another a transition needed)

        //add additional symbol
        addSymbol(tempNFA1.getNFA().getLast().transitionSymbol);

        //create temporary nfa with this new transtion
        NFA tempNFA3 = nfaStack.pop();

        //new transition needs to be placed between intial two
        //start/end states need to switch for tempNFA3 and tempNFA2

        //local variables for start/end states for better readability
        int temp1Start = tempNFA2.getNFA().getLast().startState.stateID;
        int temp3Start = tempNFA3.getNFA().getLast().startState.stateID;
        int temp1End = tempNFA2.getNFA().getLast().endState.stateID;
        int temp3End = tempNFA3.getNFA().getLast().endState.stateID;

        // System.out.println((temp1Start));
        // System.out.println((temp3Start));
        // System.out.println((temp1End));
        // System.out.println((temp3End));

        //switch start states
        tempNFA2.getNFA().getLast().startState.setStateID(temp1Start+=2);
        tempNFA3.getNFA().getLast().startState.setStateID(temp3Start-=2);

        //switch end states
        tempNFA2.getNFA().getLast().endState.setStateID(temp1End+=2);
        tempNFA3.getNFA().getLast().endState.setStateID(temp3End-=2);

        //create epsilon transitions
        Transition t1 = new Transition('e', tempNFA1.getNFA().getLast().endState, tempNFA3.getNFA().getFirst().startState);
        Transition t2 = new Transition('e', tempNFA1.getNFA().getLast().endState, tempNFA2.getNFA().getFirst().startState);
        Transition t3 = new Transition('e', tempNFA3.getNFA().getLast().endState, tempNFA3.getNFA().getFirst().startState);
        Transition t4 = new Transition('e', tempNFA3.getNFA().getLast().endState, tempNFA2.getNFA().getFirst().startState);

        //add epsilon transitions between first two temp nfa's
        tempNFA1.getNFA().add(t1);
        tempNFA1.getNFA().add(t2);

        //add 'second' temp nfa to first
        for (int i=0; i<tempNFA3.getNFA().size(); i++) {
            tempNFA1.getNFA().add(tempNFA3.getNFA().get(i).getTransition());
        }

        //add epsilon transitions between 'second' nfa and 'third'
        tempNFA1.getNFA().add(t3);
        tempNFA1.getNFA().add(t4);

        //add 'third' nfa to the first
        for (int i=0; i<tempNFA2.getNFA().size(); i++) {
            tempNFA1.getNFA().add(tempNFA2.getNFA().get(i).getTransition());
        }

        //add NFA back into nfa stack
        nfaStack.push(tempNFA1);
    }//union

    //concatentation (.) operation in Thompson's algorithm
    private void concat() {

        NFA tempNFA2 = nfaStack.pop();
        NFA tempNFA1 = nfaStack.pop();

        // System.out.println("CONCAT temp NFA base");
        // for (int j=0; j< tempNFA2.getNFA().size(); j++) {
        //     System.out.println(tempNFA2.getNFA().get(j).transitionString());
        // }

        //create epsilon transition from the end of tempNFA1 to tempNFA2
        //System.out.println("Trans..Start ID: " + tempNFA2.getNFA().getLast().endState.stateID);
        //System.out.println("Trans..End ID: " + tempNFA1.getNFA().getFirst().startState.stateID);
        Transition t1 = new Transition('e', tempNFA1.getNFA().getLast().endState, tempNFA2.getNFA().getFirst().startState);

        //add new epsilon transition to tempNFA1
        tempNFA1.getNFA().add(t1);

        //combine states into one NFA
        for (int i=0; i<tempNFA2.getNFA().size(); i++) {
            tempNFA1.getNFA().add(tempNFA2.getNFA().get(i).getTransition());
        }

        //add NFA back into stack
        nfaStack.push(tempNFA1);
    }//concatenation

    //kleene star (*) operation in Thompson's algorithm
    private void kleene() {

        //get top NFA from stack
        NFA tempNFA = nfaStack.pop();

        //get previous transition
        Transition prev = tempNFA.getNFA().getLast().getTransition();

        //get previous transition start/end states
        int prevStartID = prev.startState.stateID;
        int prevEndID = prev.endState.stateID;

        // System.out.println("PREVIOUS");
        // System.out.println(prev.transitionSymbol);
        // System.out.println(prevStartID);
        // System.out.println(prevEndID);

        //epsilon transitions are added before and after symbol transition
        //state ID's must be updated accordingly

        //create new start/end states
        State start = new State(prevStartID);
        //System.out.println("State ID: " + start.stateID);
        State next = new State(prevEndID+=2);
        //System.out.println("State ID: " + next.stateID);

        //update prev state IDs
        prev.startState.setStateID(prevStartID+=1);
        //System.out.println("State ID: " + prev.startState.stateID);
        prev.endState.setStateID(prevEndID-=1);
        //System.out.println("State ID: " + prev.endState.stateID);

        //create epsilon transitions
        Transition t1 = new Transition('e', start, tempNFA.getNFA().getFirst().startState);
        Transition t2 = new Transition('e', start, next);

        Transition t3 = new Transition('e', tempNFA.getNFA().getLast().endState, tempNFA.getNFA().getLast().startState);
        Transition t4 = new Transition('e', tempNFA.getNFA().getLast().endState, next);

        //clear initial transitions
        tempNFA.getNFA().clear();

        //add new epsilon transitions
        tempNFA.getNFA().add(t1);
        tempNFA.getNFA().add(t2);
        tempNFA.getNFA().add(prev);
        tempNFA.getNFA().add(t3);
        tempNFA.getNFA().add(t4);

        //add new nfa to stack
        nfaStack.push(tempNFA);
    }//kleene
}//RegexReader