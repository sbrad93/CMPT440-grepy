package edu.marist.brady;

public final class Digraph {
    private String name;

    public Digraph(String graphName) {
        this.name = graphName;
    }//Digraph constructor

    public void output(NFA nfa) {
        System.out.println("digraph " + this.name + "{");
        for (int i=0; i<nfa.getNFA().size(); i++) {
            char key = nfa.getNFA().get(i).transitionSymbol;
            System.out.println(nfa.getNFA().get(i).startState.stateID + 
                " -> " + 
                nfa.getNFA().get(i).endState.stateID + 
                " [ label = \"" + key + "\" ];");
        }
        System.out.println("}");
    }//output

    public void output(DFA dfa) {
        System.out.println("digraph " + this.name + "{");
        for (int i=0; i<dfa.getDFA().size(); i++) {
            char key = dfa.getDFA().get(i).transitionSymbol;
            System.out.println(dfa.getDFA().get(i).startState.stateID + 
                " -> " + 
                dfa.getDFA().get(i).endState.stateID + 
                " [ label = \"" + key + "\" ];");
        }
        System.out.println("}");
    }//output

}