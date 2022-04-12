package edu.marist.brady;

import java.util.LinkedList;

//NFA class
//Linked list of state transitions
public final class DFA {

    private LinkedList<Transition> dfa;

    public DFA() {
        this.dfa = new LinkedList<Transition>();
    }//NFA constructor

    public LinkedList<Transition> getDFA() {
        return dfa;
    }//getNFA

    public void setNFA(LinkedList<Transition> newDFA) {
        this.dfa = newDFA;
    }//setNFA
}