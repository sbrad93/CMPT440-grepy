package edu.marist.brady;

import java.util.ArrayList;
import java.util.List;

//NFA class
public final class NFA {

    private List<State> nfa;

    public NFA() {
        this.nfa = new ArrayList<State>();;
    }

    public List<State> getNFA() {
        return nfa;
    }

    public void setNFA(List<State> newNFA) {
        this.nfa = newNFA;
    }
}