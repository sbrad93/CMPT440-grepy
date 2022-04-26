package edu.marist.brady;

import java.util.LinkedList;

/**
 * DFA class
 * Linked list of state transitions
 */
public final class DFA {

    private LinkedList<Transition> dfa;

    public DFA() {
        this.dfa = new LinkedList<Transition>();
    } //DFA constructor

    public LinkedList<Transition> getDFA() {
        return dfa;
    } //getDFA

    public void setDFA(LinkedList<Transition> newDFA) {
        this.dfa = newDFA;
    } //setDFA
}
