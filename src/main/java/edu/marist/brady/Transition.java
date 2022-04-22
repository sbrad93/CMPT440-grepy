package edu.marist.brady;

public final class Transition {

    public State startState;
    public State endState;
    public char transitionSymbol;
    public boolean canSkip;
    public boolean isFiller;
    public boolean isRepeating;

    public Transition(char key, State start, State end) {
        this.startState = start;
        this.endState = end;
        this.transitionSymbol = key;
        this.canSkip = false;
        this.isFiller = false;
        this.isRepeating = false;
    }//Transition constructor

    //temporary string method for reading transitions
    public String transitionString() {
        return "Start ID: " + this.startState.stateID + 
        "\nNext ID: " + this.endState.stateID +
        "\nSymbol: " + this.transitionSymbol + 
        "\nIsEndStateAcceptState: " + this.endState.acceptState + 
        "\nisFiller: " + this.isFiller + 
        "\ncanSkip: " + this.canSkip + "\n";
    }//transitionString

    public Transition getTransition() {
        return this;
    }//getTransition
}