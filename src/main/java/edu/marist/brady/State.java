package edu.marist.brady;

/**
 * State class
 */
public final class State {

    private int stateID;
    private boolean acceptState;

    public State(int id) {
        this.stateID = id;
        this.acceptState = false;
        RegexReader.stateCount++;
    } //State constructor

    public State getState() {
        return this;
    } //getState

    public int getStateID() {
        return this.stateID;
    }

    public void setStateID(int newID) {
        this.stateID = newID;
    } //setState

    public boolean getAcceptState() {
        return this.acceptState;
    } //getAcceptState

    public void setAcceptState(boolean foo) {
        this.acceptState = foo;
    } //setAcceptState
}
