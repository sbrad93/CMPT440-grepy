package edu.marist.brady;

import java.util.LinkedList;
import java.util.List;

//NFA class
public final class NFA {

    //private List<State> nfa;

    private LinkedList<Transition> nfa;

    public NFA() {
        //this.nfa = new LinkedList<State>();

        this.nfa = new LinkedList<Transition>();
    }

    public LinkedList<Transition> getNFA() {
        return nfa;
    }

    public void setNFA(LinkedList<Transition> newNFA) {
        this.nfa = newNFA;
    }
}