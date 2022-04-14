package edu.marist.brady;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class Transition {

    public State startState;
    public State endState;
    public char transitionSymbol;
    private static Map<Character, ArrayList<Entry<State, State>>> transitionMap = new HashMap<Character, ArrayList<Entry<State, State>>>();
    private static ArrayList<Entry<State, State>> transitions;

    public Transition(char key, State start, State end) {
        this.startState = start;
        this.endState = end;
        this.transitionSymbol = key;
    }//Transition constructor

    public static void addToMap(Transition transition) {
        //list of states with a given key transition
        transitions = transitionMap.get(transition.transitionSymbol);

        if (transitions == null) {
            transitions = new ArrayList<Entry<State, State>>();
            transitionMap.put(transition.transitionSymbol,transitions);
 }
        //add start/end pair to transition list
        //https://stackoverflow.com/questions/6121246/list-of-entries-how-to-add-a-new-entry
        transitions.add(new java.util.AbstractMap.SimpleEntry<State, State>(transition.startState, transition.endState));

    }

    //temporary string method for reading transitions
    public String transitionString() {
        return "Start ID: " + this.startState.stateID + 
        "\nNext ID: " + this.endState.stateID +
        "\nSymbol: " + this.transitionSymbol + 
        "\nIsEndStateAcceptState: " + this.endState.acceptState + "\n";
    }

    public Transition getTransition() {
        return this;
    }//getTransition

    public static Map<Character, ArrayList<Entry<State, State>>> getTransitionMap() {
        return transitionMap;
    }//getTransitionMap

    public static ArrayList<Entry<State, State>> getAllTransitions(char c) {
        return transitionMap.get(c);
    }//getAllTransitions

    public static void clearTransitions() {
        transitionMap.clear();
    }

    // public void setTransitionSymbol(char newSym) {
    //     this.transitionSymbol = newSym;
    //     this.addToMap();
    // }
}