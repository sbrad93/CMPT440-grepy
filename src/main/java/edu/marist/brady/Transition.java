package edu.marist.brady;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public final class Transition {

    public State startState;
    public State endState;
    private char transitionSymbol;
    private Map<Character, ArrayList<State>> transitionMap;
    private ArrayList<State> transitions;

    public Transition(char key, State start, State next) {
        this.startState = start;
        this.endState = next;
        this.transitionSymbol = key;

        this.transitionMap = new TreeMap<Character, ArrayList<State>>();

        transitions = this.transitionMap.get(key);

        if (transitions == null) {
            transitions = new ArrayList<State>();
            this.transitionMap.put(key,transitions);
        }

        transitions.add(next);
    }

    //temporary string method for reading transitions
    public String transitionString() {
        return "Start ID: " + this.startState.stateID + 
        "\nNext ID: " + this.endState.stateID +
        "\nSymbol: " + this.transitionSymbol + "\n";
    }

    public Transition getTransition() {
        return this;
    }
}