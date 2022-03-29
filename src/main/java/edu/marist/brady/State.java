package edu.marist.brady;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

//State class
public final class State {

    private int stateID;
    private Map<Character, ArrayList<State>> nextState;
    private boolean acceptState;

    public State(int ID) {
        this.stateID = -1;
        this.nextState = new TreeMap<Character, ArrayList<State>>();
        this.acceptState = false;
    }

    public void addTransition (State nextState, char key) {
        ArrayList <State> transitions = this.nextState.get(key);

        if (transitions == null) {
            transitions = new ArrayList<State>();
            this.nextState.put(key,transitions);
        }

        transitions.add(nextState);
    }// addTransition


}
