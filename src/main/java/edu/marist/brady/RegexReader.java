package edu.marist.brady;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    }

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

        System.out.println(isAlphabet('x'));
        System.out.println(isAlphabet('a'));
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

    private int precedence(char c) {
        if(c == '*') return 4;
        else if(c == '+') return 3;
        else if(c == '.') return 2;
        else return 1;
    }

    //creates nfa based on regex expression
    public void createNFA() {

        String regex = addConcat(myRegex);
        int addedSymbols = 0;

        nfaStack.clear();
        operators.clear();

        for (int i=0; i<regex.length(); i++) {

            if (isAlphabet(regex.charAt(i)) && addedSymbols < 2) {
                addSymbol(regex.charAt(i));
                addedSymbols++;
            }

            // else if (addedSymbols == 2) {
            //     addedSymbols = 0;
            //     compute();
            // }

            else if (regex.charAt(i) == '*') {
                operators.add(regex.charAt(i));
                compute();
                //kleene();
            }

            else if (regex.charAt(i) == '.') {
                operators.add(regex.charAt(i));
                // addSymbol(regex.charAt(i+1));
                // concat();
            }

            if (addedSymbols == 2) {
                addedSymbols--;
                compute();
            }

            // else {
            //     while (!operators.isEmpty() && precedence(regex.charAt(i)) > precedence(operators.get(operators.size()-1)))
            //     //switch statement
            //     compute();
            // }

        }

        //testing ---------------------------
        for (int i=0; i<nfaStack.size(); i++){
            for (int j=0; j< nfaStack.get(i).getNFA().size(); j++) {
                System.out.println("NFA " + i);
                System.out.println(nfaStack.get(i).getNFA().get(j).transitionString());
            }
        }
        System.out.println("State Count: " + stateCount);

        System.out.println("NFA stack size: " + nfaStack.size());
        //------------------------------------
    }

    //adds nfa transition for alphabet symbols
    private void addSymbol(char sym) {
        State start = new State (stateCount);
        State end = new State (stateCount);

        //add symbol transition from start state to next state
        Transition t1 = new Transition(sym, start, end);
        
        NFA tempNFA = new NFA();

        tempNFA.getNFA().add(t1);

        //start.addTransition(end, sym);
        //tempNFA.getNFA().add(start);
        //tempNFA.getNFA().add(end);

        nfaStack.push(tempNFA);
    }//addSymbol

    //switch statement based on regex operation
    private void compute() {
        if(RegexReader.operators.size() > 0) {

            char charAt = operators.pop();

            switch(charAt) {
                case('+'):
                    //union();
                    break;
                
                case('.'):
                    System.out.println("ayo");
                    concat();
                    break;
                
                case('*'):
                    kleene();
                    break;

                default:
                    System.exit(1);
                    break;
            }
        }
    }//compute

    //union operation in Thompson's algorithm
    private void union() {
        // NFA tempNfa1 = nfaStack.pop();
        // NFA tempNfa2 = nfaStack.pop();

        // //new states to compute union operation
        // State start = new State(stateCount);
        // State next = new State(stateCount);

        // Transition t1 = new Transition('e', start, tempNfa1.getNFA().getFirst().startState);
        // Transition t2 = new Transition('e', start, tempNfa2.getNFA().getFirst().startState);

    }//union

    //concatentation operation in Thompson's algorithm
    private void concat() {
        NFA tempNFA1 = nfaStack.pop();
        NFA tempNFA2 = nfaStack.pop();

        //create epsilon transition from the end of tempNFA1 to tempNFA2
        Transition t1 = new Transition('e', tempNFA2.getNFA().getLast().endState, tempNFA1.getNFA().getFirst().startState);

        //add new epsilon transition to tempNFA1
        tempNFA2.getNFA().add(t1);

        //combine states into one NFA
        for (int i=0; i<tempNFA1.getNFA().size(); i++) {
            tempNFA2.getNFA().add(tempNFA1.getNFA().get(i).getTransition());
        }

        //add NFA back into stack
        nfaStack.push(tempNFA2);

    }//concatenation

    //kleene star operation in Thompson's algorithm
    private void kleene() {
        //get top NFA from stack
        NFA tempNFA = nfaStack.pop();

        //get last transition
        Transition prev = tempNFA.getNFA().getLast().getTransition();

        //create new states
        State start = new State(stateCount);
        State next = new State(stateCount);

        //epsilon transitions
        Transition t1 = new Transition('e', start, next);
        Transition t2 = new Transition('e', start, tempNFA.getNFA().getFirst().startState);

        Transition t3 = new Transition('e', tempNFA.getNFA().getLast().endState, next);
        Transition t4 = new Transition('e', tempNFA.getNFA().getLast().endState, tempNFA.getNFA().getLast().startState);

        //add transitions to tempNFA
        tempNFA.getNFA().clear();

        tempNFA.getNFA().add(t1);
        tempNFA.getNFA().add(t2);
        tempNFA.getNFA().add(prev);
        tempNFA.getNFA().add(t3);
        tempNFA.getNFA().add(t4);

        nfaStack.push(tempNFA);

    }//kleene

}