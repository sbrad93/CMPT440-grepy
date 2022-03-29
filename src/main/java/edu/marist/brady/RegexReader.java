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
    private Stack<NFA> nfaStack = new Stack<NFA>();
    private Stack<Character> operators = new Stack<Character>();

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
                        (regexChars[i] != '*')) {
                    myAlphabet.add(regexChars[i]);
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //testing testing testing
        System.out.println(myAlphabet);

        System.out.println(myRegex);

        myRegex = this.addConcat(myRegex);
        System.out.println(myRegex);

        System.out.println(isAlphabet('x'));
        System.out.println(isAlphabet('a'));
        //

    } //read

    //returns a set of all non-operator symbols in language
    public char[] getChars(String str) {
        char[] chars = new char[str.length()];
        for (int i= 0; i<str.length(); i++) {
            chars[i] = str.charAt(i);
        }
        return chars;
    }//getChars

    // returns boolean indicating whether given character is part of language
    private boolean isAlphabet(char charIn) {
        boolean ans = false;

        for (int i=0; i<myAlphabet.size(); i++) {
            if (myAlphabet.contains(charIn))
                ans = true;
        }

        return ans;
    } //isAlphabet

    //inserts "." symbol wherever concatenation is necessary
    public String addConcat(String regex) {
        String res = "";

        for(int i=0; i<regex.length()-1; i++) {
            if (isAlphabet(regex.charAt(i)) && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (isAlphabet(regex.charAt(i)) && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i) == ')' && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i+1) == ')' && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i) == '*' && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i+1) == '*' && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else res += regex.charAt(i);
        }

        return res;
    }//addConcat


    // public NFA createNFA(String regex) {
    //     regex = addConcat(regex);


    // }
}