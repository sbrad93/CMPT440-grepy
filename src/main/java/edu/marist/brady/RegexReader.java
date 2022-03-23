package edu.marist.brady;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public final class RegexReader {
    private String myRegex;
    private HashSet<Character> myAlphabet;

    protected RegexReader() {
        this.myRegex = "";
        this.myAlphabet = new HashSet<Character>();
    }

    public void read() {

        try {
            File myObj = new File("/Users/shannonbrady/Desktop/CMPT440-grepy/filename.txt");
            Scanner myReader = new Scanner(myObj);
            myRegex = myReader.nextLine();

            char[] regexChars = new char[myRegex.length()];
            
            for (int i= 0; i<myRegex.length(); i++) {
                regexChars[i] = myRegex.charAt(i);
            }
            
            // loops through characters to build alphabet
            for (int i= 0; i<myRegex.length(); i++) {
                if ((regexChars[i] != '(') &&
                        (regexChars[i] != ')') &&
                        (regexChars[i] != '+') && 
                        (regexChars[i] != '*')) {
                    myAlphabet.add(regexChars[i]);
                }
            }
		
		System.out.println(myAlphabet);
        System.out.println(isAlphabet('a'));
        System.out.println(isAlphabet('x'));

        
        this.addConcat(myRegex);


            // while (myReader.hasNextLine()) {
            //     String data = myReader.nextLine();
            //     System.out.println(data);
            // }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // returns boolean indicating whether given character is part of language
    private boolean isAlphabet(char charIn) {
        boolean ans = false;
        if (charIn == 'a')
            ans = true;
        else if (charIn == 'b')
            ans = true;
        else if (charIn == 'c')
            ans = true;
        else if (charIn == 'e')
            ans = true;
        else ans = false;
        return ans;
    }




    public String addConcat(String regex) {
        // String[] symbols = {"+", "*", ".", "(", ")"};
        // ArrayList<String> symbolsList =  new ArrayList<>(Arrays.asList(symbols));

        // ArrayList<String> res = new ArrayList<String>();

        // // split regex string
        // String[] regexSplit = myRegex.split("");

        // List<String> regexList = new ArrayList<>(Arrays.asList(regexSplit));
        // System.out.println(regexList);

        // for (int i=0; i<regexList.size(); i++) {
        //     res.add(regexList.get(i));
        //     if (!(regexList.get(i).contains("(")) &&
        //         !(regexList.get(i).contains(")")) &&
        //         !(regexList.get(i).contains("+")) &&
        //         !(regexList.get(i).contains("*"))) 
        //         if (!(regexList.get(i+1).contains("(")) &&
        //             !(regexList.get(i).contains(")")) &&
        //             !(regexList.get(i).contains("+")) &&
        //             !(regexList.get(i).contains("*")) |
        //             !(regexList.get(i+1).contains("("))) {
        //                 res.add(".");
        //         }

            
        // }
        // System.out.println(res);

        String res = "";

        for(int i=0; i<regex.length()-1; i++) {
            if (isAlphabet(regex.charAt(i)) && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (isAlphabet(regex.charAt(i)) && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i+1) == ')' && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i+1) == ')' && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i) == '*' && isAlphabet(regex.charAt(i+1)))
                res += regex.charAt(i) + ".";
            else if (regex.charAt(i+1) == '*' && regex.charAt(i+1) == '(')
                res += regex.charAt(i) + ".";
            else res += regex.charAt(i);

        }

        System.out.println(myRegex);
        System.out.println(res);
        return res;
    }
}