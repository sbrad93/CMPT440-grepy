package edu.marist.brady;

//State class
public final class State {

    public int stateID;
    public boolean acceptState;

    public State(int ID) {
        this.stateID = ID;
        this.acceptState = false;
        RegexReader.stateCount++;
    }//State constructor

    public State getState() {
        return this;
    }//getState

    public void setStateID(int newID) {
        this.stateID = newID;
    }//setState

    public void setAcceptState(boolean foo) {
        this.acceptState = foo;
    }//setAcceptState
}
