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

            if (myAlphabet.size() > 2) {
                System.out.println("This program only processes two input symbols. Please try again. Or don't.");
                System.exit(0);
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

    //outputs transition map in string format
    private void printTransitionMap() {
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
    }

    //outputs NFA in string format
    private void printNFA(NFA nfa) {
        for (int j=0; j< nfa.getNFA().size(); j++)
        System.out.println(nfa.getNFA().get(j).transitionString());
    }

    //outputs DFA in string format
    private void printDFA(DFA dfa) {
        for (int j=0; j< dfa.getDFA().size(); j++)
        System.out.println(dfa.getDFA().get(j).transitionString());
    }

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
        }
        res += regex.charAt(regex.length() - 1)+"";
        return res;
    }//addConcat

    //reorders regex string to match operator precedence
    public String orderRegex(String regex) {
        String kleeneString = "";
        String unionString = "";
        String symbolString = "";
        String res = "";
        String[] regexParts = regex.split("\\.");

        for (int i=0; i<regexParts.length; i++) {
            // System.out.println(regexParts.length);
            // System.out.println(regexParts[i]);
            if (regexParts[i].contains("*") )
                kleeneString += regexParts[i];
            else if (regexParts[i].contains("+"))
                unionString += regexParts[i];
            else symbolString += regexParts[i];
        }

        res = kleeneString + symbolString + unionString;
        
        return res;
    }

    //creates nfa based on regex expression
    public NFA createNFA() {

        NFA finalNFA = null;

        //add concatenation to inital regex
        String regex = addConcat(myRegex);

        //indicator for computing concatenation
        int addedSymbols = 0;

        //clear da stacks
        nfaStack.clear();
        operators.clear();

        for (int i=0; i<regex.length(); i++) {

            //add input symbols
            if (isAlphabet(regex.charAt(i)) && addedSymbols < 2) {
                addSymbol(regex.charAt(i));
                addedSymbols++;
            }
            //concatenation or union
            else if (regex.charAt(i) == '+' || regex.charAt(i) == '.') {
                operators.add(regex.charAt(i));
            }
            //kleene star
            else if (regex.charAt(i) == '*') {
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
        finalNFA.getNFA().get(finalNFA.getNFA().size()-1).endState.setAcceptState(true);

        //add transitions to transition map
        for (int i=0; i<finalNFA.getNFA().size(); i++) {
            Transition.addToMap(finalNFA.getNFA().get(i));
        }

        //testing ---------------------------

        printNFA(finalNFA);
        //printTransitionMap();

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
    private void union() {
        NFA tempNFA2 = nfaStack.pop();
        NFA tempNFA1 = nfaStack.pop();

        //at this point there are only two symbols added (ex: a+b --> only four states, 2 transitions)
        //another symbol must be added for the middle portion of final nfa (ex: a+b ---> another a transition needed)

        //add additional symbol
        addSymbol(tempNFA1.getNFA().getLast().transitionSymbol);

        //create temporary nfa with this new transtion
        NFA tempNFA3 = nfaStack.pop();

        // System.out.println("TEMP NFA 1");
        // for (int i=0; i<tempNFA1.getNFA().size(); i++) {
        //     System.out.println(tempNFA1.getNFA().get(i).transitionString());
        // }
        // System.out.println("------------------------------------------------");

        // System.out.println("TEMP NFA 2");
        // for (int i=0; i<tempNFA2.getNFA().size(); i++) {
        //     System.out.println(tempNFA2.getNFA().get(i).transitionString());
        // }
        // System.out.println("------------------------------------------------");

        // System.out.println("TEMP NFA 3");
        // for (int i=0; i<tempNFA3.getNFA().size(); i++) {
        //     System.out.println(tempNFA3.getNFA().get(i).transitionString());
        // }
        // System.out.println("------------------------------------------------");

        //new transition needs to be placed between intial two
        //start/end states need to switch for tempNFA3 and tempNFA2

        //local variables for start/end states for better readability
        int temp1Start = tempNFA2.getNFA().getLast().startState.stateID;
        int temp3Start = tempNFA3.getNFA().getLast().startState.stateID;
        int temp1End = tempNFA2.getNFA().getLast().endState.stateID;
        int temp3End = tempNFA3.getNFA().getLast().endState.stateID;

        //switch start states
        tempNFA2.getNFA().getLast().startState.setStateID(temp1Start+2);
        tempNFA3.getNFA().getLast().startState.setStateID(temp3Start-2);

        //switch end states
        tempNFA2.getNFA().getLast().endState.setStateID(temp1End+2);
        tempNFA3.getNFA().getLast().endState.setStateID(temp3End-2);

        // System.out.println("TEMP NFA 1");
        // for (int i=0; i<tempNFA1.getNFA().size(); i++) {
        //     System.out.println(tempNFA1.getNFA().get(i).transitionString());
        // }
        // System.out.println("------------------------------------------------");

        // System.out.println("TEMP NFA 2");
        // for (int i=0; i<tempNFA2.getNFA().size(); i++) {
        //     System.out.println(tempNFA2.getNFA().get(i).transitionString());
        // }
        // System.out.println("------------------------------------------------");

        // System.out.println("TEMP NFA 3");
        // for (int i=0; i<tempNFA3.getNFA().size(); i++) {
        //     System.out.println(tempNFA3.getNFA().get(i).transitionString());
        // }
        // System.out.println("------------------------------------------------");

        //create epsilon transitions
        Transition t1 = new Transition('e', tempNFA1.getNFA().getLast().endState, tempNFA3.getNFA().getFirst().startState);
        Transition t2 = new Transition('e', tempNFA1.getNFA().getLast().endState, tempNFA2.getNFA().getFirst().startState);
        Transition t3 = new Transition('e', tempNFA3.getNFA().getLast().endState, tempNFA3.getNFA().getFirst().startState);
        Transition t4 = new Transition('e', tempNFA3.getNFA().getLast().endState, tempNFA2.getNFA().getFirst().startState);

        //add all transitions to tempNFA1
        tempNFA1.getNFA().add(t1);

        for (int i=0; i<tempNFA3.getNFA().size(); i++) {
            tempNFA1.getNFA().add(tempNFA3.getNFA().get(i).getTransition());
        }

        tempNFA1.getNFA().add(t3);
        tempNFA1.getNFA().add(t2);
        tempNFA1.getNFA().add(t4);

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

        //create epsilon transition from tempNFA1 to tempNFA2
        Transition t1 = new Transition('e', tempNFA1.getNFA().getLast().endState, tempNFA2.getNFA().getFirst().startState);
            
        // System.out.println("CONCATENATION");
        // System.out.println(t1.transitionString());
        // System.out.println("------------------------------------------");

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

        //epsilon transitions are added before and after symbol transition
        //state ID's are updated accordingly

        NFA tempNFA = nfaStack.pop();
        NFA finalNFA = new NFA();

        // System.out.println("KLEENE TEMP NFA: 1");
        // for (int i=0; i<tempNFA.getNFA().size(); i++) {
        //     System.out.println(tempNFA.getNFA().get(i).transitionString());
        // }
        // System.out.println("------------------------------------------");

        //remove previous transition
        Transition prev = tempNFA.getNFA().removeLast();

        //previous transition start/end states
        int prevStartID = prev.startState.stateID;
        int prevEndID = prev.endState.stateID;

        //create new start/end states
        State start = new State(prevStartID);
        State next = new State(prevEndID+2);

        //change end state state pointer to new 'start' state
        if (tempNFA.getNFA().size()>1) {
            tempNFA.getNFA().get(tempNFA.getNFA().size()-1).endState = start.getState();
        }

        //update prev state IDs
        prev.startState.setStateID(prevStartID+1);
        prev.endState.setStateID(prevEndID+1);

        // //create epsilon transitions
        Transition t1 = new Transition('e', start, prev.startState);
        Transition t2 = new Transition('e', start, next);
        Transition t3 = new Transition('e', prev.endState, prev.startState);
        Transition t4 = new Transition('e', prev.endState, next);

        //add all transitions from tempNFA to finalNFA
        if (tempNFA.getNFA().size() > 1) {
            for(int i=0; i<tempNFA.getNFA().size(); i++) {
                finalNFA.getNFA().add(tempNFA.getNFA().get(i));
            }
        }

        //add new transitions and prev
        finalNFA.getNFA().add(t1);
        finalNFA.getNFA().add(prev);
        finalNFA.getNFA().add(t3);
        finalNFA.getNFA().add(t2);
        finalNFA.getNFA().add(t4);

        // System.out.println("KLEENE TEMP NFA: 2");
        // for (int i=0; i<tempNFA.getNFA().size(); i++) {
        //     System.out.println(tempNFA.getNFA().get(i).transitionString());
        // }
        // System.out.println("------------------------------------------");
        
        //add new nfa to stack
        nfaStack.push(finalNFA);
    }//kleene

    private boolean isOnlyKleene(String regex) {
        boolean ans = false;
        char[] chars = getChars(regex);

        for (int i=0; i<chars.length; i++) {
            if (chars[i] == '+') {
                ans = false;
            }
            else ans = true;
        }
        return ans;
    }

    //converts nfa to dfa
    public DFA createDFA(NFA nfa) {
        DFA dfa = new DFA();

        //clear transition map
        Transition.clearTransitions();

        //mark reverse epsilon transitions accordingly
        for (int i=0; i<nfa.getNFA().size(); i++) {
            if (i != 0 && nfa.getNFA().get(i).endState.stateID < nfa.getNFA().get(i).startState.stateID) {
                nfa.getNFA().get(i).transitionSymbol = '%';
            }
        }

        //add all non-epsilon transitions to dfa
        //convert marked transitions
        for (int i=0; i<nfa.getNFA().size(); i++) {
            if (nfa.getNFA().get(i).transitionSymbol != 'e') {
                if (i!=0 && nfa.getNFA().get(i).transitionSymbol == '%') {
                    nfa.getNFA().get(i-1).endState.setStateID(nfa.getNFA().get(i).endState.stateID);
                }
                else dfa.getDFA().add(nfa.getNFA().get(i));
            }
        }

        //connect end states to the correct start state as needed
        for (int i=0; i<dfa.getDFA().size(); i++) {
            if (i!=0 && dfa.getDFA().get(i).startState != dfa.getDFA().get(i-1).endState) {
                if (dfa.getDFA().get(i-1).startState.stateID < dfa.getDFA().get(i-1).endState.stateID) {
                    dfa.getDFA().get(i-1).endState = dfa.getDFA().get(i).startState;
                }
                else if (dfa.getDFA().get(i-1).startState.stateID > dfa.getDFA().get(i-1).endState.stateID) {
                    dfa.getDFA().get(i).startState = dfa.getDFA().get(i-1).startState;
                }
            }
        }

        //connect kleene star operations if necessary
        for (int i=0; i<dfa.getDFA().size(); i++) {
            if (i != 0 && dfa.getDFA().get(i).startState.stateID == dfa.getDFA().get(i).endState.stateID &&
                dfa.getDFA().get(i-1).startState.stateID == dfa.getDFA().get(i-1).endState.stateID) {
                    Transition t = new Transition (dfa.getDFA().get(i).transitionSymbol, dfa.getDFA().get(i-1).endState, dfa.getDFA().get(i).startState);
                    dfa.getDFA().add(t);
            }
        }

        //add accept states if myRegex is only kleene operations
        for (int i=0; i<dfa.getDFA().size(); i++) {
            if (isOnlyKleene(myRegex) && dfa.getDFA().get(i).startState.stateID == dfa.getDFA().get(i).endState.stateID) {
                dfa.getDFA().get(i).endState.acceptState = true;
            }
        }

        //add dfa transitions to transition map
        for (int i=0; i<dfa.getDFA().size(); i++) {
            Transition.addToMap(dfa.getDFA().get(i));
        }

        //testing -------------------
        System.out.println("---------------------------------------------------------------");
        System.out.println("DFA");
        printDFA(dfa);
        printTransitionMap();
        //---------------------------

        return dfa;

    }//createDFA

}//RegexReader