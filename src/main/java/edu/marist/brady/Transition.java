package edu.marist.brady;

/**
 * Transition class
 */
public final class Transition {

    private State startState;
    private State endState;
    private char transitionSymbol;
    public boolean canSkip;
    public boolean isFiller;
    public boolean isRepeating;

    /**
     * Transition constructor.
     */
    public Transition(char key, State start, State end) {
        this.startState = start;
        this.endState = end;
        this.transitionSymbol = key;
        this.canSkip = false;
        this.isFiller = false;
        this.isRepeating = false;
    }

    //temporary string method for reading transitions
    public String transitionString() {
        return "Start ID: " + this.startState.getStateID() + 
        "\nNext ID: " + this.endState.getStateID() +
        "\nSymbol: " + this.transitionSymbol + 
        "\nIsEndStateAcceptState: " + this.endState.getAcceptState() + 
        "\nisFiller: " + this.isFiller + 
        "\ncanSkip: " + this.canSkip + "\n";
    } //transitionString

    public Transition getTransition() {
        return this;
    } //getTransition

    public char getTransitionSymbol() {
        return this.transitionSymbol;
    } //getTransitionSymbol

    public void setTransitionSymbol(char newSym) {
        this.transitionSymbol = newSym;
    } //setTransitionSymbol

    public State getStartState() {
        return this.startState;
    } //getStartState

    public void setStartState(State newState) {
        this.startState = newState;
    } //setStartState

    public State getEndState() {
        return this.endState;
    } //getEndState

    public void setEndState(State newState) {
        this.endState = newState;
    } //setEndState
}
