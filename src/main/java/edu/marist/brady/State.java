package edu.marist.brady;

//State class
public final class State {

    public int stateID;
    //private Map<Character, ArrayList<State>> nextState;
    public boolean acceptState;

    public State(int ID) {
        this.stateID = ID;
        //this.nextState = new TreeMap<Character, ArrayList<State>>();
        this.acceptState = false;
        RegexReader.stateCount++;
    }

    // public void addTransition (State nextState, char key) {
    //     ArrayList <State> transitions = this.nextState.get(key);

    //     if (transitions == null) {
    //         transitions = new ArrayList<State>();
    //         this.nextState.put(key,transitions);
    //     }

    //     transitions.add(nextState);
    // }// addTransition




}
