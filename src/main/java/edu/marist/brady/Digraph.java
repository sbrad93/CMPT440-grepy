package edu.marist.brady;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Digraph class.
 */
public final class Digraph {
    private String name;
    private HashSet<Transition> transitions;
    private String nodeShape;

    /**
     * Digraph constructor.
     */
    public Digraph(String graphName) {
        this.name = graphName;
    }

    //NFA methods ---------------------------------------------
    //writes DOT output to file for NFA
    public void output(NFA nfa) {
        try {
            FileWriter writer = new FileWriter("nfa.txt");

            this.findNodes(nfa);
            writer.write("digraph " + this.name + " {\n\n");

            for (Transition t : transitions) {
                if (t.getEndState().getAcceptState()) {
                    nodeShape = "doublecircle";
                }
                else nodeShape = "circle";
                writer.write("node [ shape = " + nodeShape + " ]; " + t.getEndState().getStateID() + ";\n");
            }

            writer.write("\n");

            for (int i=0; i<nfa.getNFA().size(); i++) {
                char key = nfa.getNFA().get(i).getTransitionSymbol();
                writer.write(nfa.getNFA().get(i).getStartState().getStateID() + 
                    " -> " + 
                    nfa.getNFA().get(i).getEndState().getStateID() + 
                    " [ label = \"" + key + "\" ];\n");
            }

            writer.write("\n}");
            writer.close();
        } catch (IOException e) {
            System.out.println("Something blew up.");
            e.printStackTrace();
        }
    } //output for NFA

    //builds set of transitions in NFA
    private void findNodes(NFA nfa) {
        transitions = new HashSet<Transition>();

        for (int i=0; i<nfa.getNFA().size(); i++) {
            if (!nfa.getNFA().get(i).isFiller) {
                transitions.add(nfa.getNFA().get(i));
            }
        }
    } //findNodes for NFA

    //----------------------------------------------------------

    //DFA methods ----------------------------------------------
    //writes DOT output to file for DFA
    public void output(DFA dfa) {
        try {
            FileWriter writer = new FileWriter("dfa.txt");

            this.findNodes(dfa);
            writer.write("digraph " + this.name + " {\n\n");

            for (Transition t : transitions) {
                if (t.getEndState().getAcceptState()) {
                    nodeShape = "doublecircle";
                }
                else nodeShape = "circle";
                writer.write("node [ shape = " + nodeShape + " ]; " + t.getEndState().getStateID() + ";\n");
            }

            writer.write("\n");

            for (int i=0; i<dfa.getDFA().size(); i++) {
                char key = dfa.getDFA().get(i).getTransitionSymbol();
                writer.write(dfa.getDFA().get(i).getStartState().getStateID() + 
                    " -> " + 
                    dfa.getDFA().get(i).getEndState().getStateID() + 
                    " [ label = \"" + key + "\" ];\n");
            }

            writer.write("\n}");
            writer.close();
        } catch (IOException e) {
            System.out.println("Something blew up.");
            e.printStackTrace();
        }
    } //output for DFA

    //builds set of transitions in DFA
    private void findNodes(DFA dfa) {
        transitions = new HashSet<Transition>();
        
        for (int i=0; i<dfa.getDFA().size(); i++) {
            if (!dfa.getDFA().get(i).isFiller) {
                transitions.add(dfa.getDFA().get(i));
            }
        }
    } //findNodes for DFA

    //----------------------------------------------------------
}
