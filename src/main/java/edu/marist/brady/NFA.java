package edu.marist.brady;

import java.util.LinkedList;

/**
 * NFA class.
 * Linked list of state transitions
 */
public final class NFA {

    private LinkedList<Transition> nfa;

    /**
     * NFA constructor.
     */
    public NFA() {
        this.nfa = new LinkedList<Transition>();
    }

    public LinkedList<Transition> getNFA() {
        return nfa;
    } //getNFA

    public void setNFA(LinkedList<Transition> newNFA) {
        this.nfa = newNFA;
    } //setNFA
}
