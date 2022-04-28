package edu.marist.brady;

/**
 * State class.
 */
public final class State {

    private int stateID;
    private boolean acceptState;

    /**
     * State constructor.
     */
    public State(int id) {
        this.stateID = id;
        this.acceptState = false;
        RegexReader.stateCount++;
    }

    public State getState() {
        return this;
    } //getState

    public int getStateID() {
        return this.stateID;
    } //getStateID

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
