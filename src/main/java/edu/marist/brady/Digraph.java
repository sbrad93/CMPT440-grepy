package edu.marist.brady;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public final class Digraph {
    private String name;
    private HashSet<Transition> transitions;
    private String nodeShape;

    public Digraph(String graphName) {
        this.name = graphName;
    }//Digraph constructor

    //NFA methods ---------------------------------------------
    //writes DOT output to file for NFA
    public void output(NFA nfa) {
        try {
            FileWriter writer = new FileWriter("nfa.txt");

            this.findNodes(nfa);
            writer.write("digraph " + this.name + " {\n\n");

            for (Transition t : transitions) {
                if (t.endState.acceptState) {
                    nodeShape = "doublecircle";
                }
                else nodeShape = "circle";
                writer.write("node [ shape = " + nodeShape + " ]; " + t.endState.stateID + ";\n");
            }

            writer.write("\n");

            for (int i=0; i<nfa.getNFA().size(); i++) {
                char key = nfa.getNFA().get(i).transitionSymbol;
                writer.write(nfa.getNFA().get(i).startState.stateID + 
                    " -> " + 
                    nfa.getNFA().get(i).endState.stateID + 
                    " [ label = \"" + key + "\" ];\n");
            }

            writer.write("\n}");
            writer.close();
        } catch (IOException e) {
            System.out.println("Something blew up.");
            e.printStackTrace();
        }
    }//output for NFA

    //builds set of transitions in NFA
    private void findNodes(NFA nfa) {
        transitions = new HashSet<Transition>();

        for (int i=0; i<nfa.getNFA().size(); i++) {
            if (!nfa.getNFA().get(i).isFiller) {
                transitions.add(nfa.getNFA().get(i));
            }
        }
    }//findNodes for NFA

    //----------------------------------------------------------

    //DFA methods ----------------------------------------------
    //writes DOT output to file for DFA
    public void output(DFA dfa) {
        try {
            FileWriter writer = new FileWriter("dfa.txt");

            this.findNodes(dfa);
            writer.write("digraph " + this.name + " {\n\n");

            for (Transition t : transitions) {
                if (t.endState.acceptState) {
                    nodeShape = "doublecircle";
                }
                else nodeShape = "circle";
                writer.write("node [ shape = " + nodeShape + " ]; " + t.endState.stateID + ";\n");
            }

            writer.write("\n");

            for (int i=0; i<dfa.getDFA().size(); i++) {
                char key = dfa.getDFA().get(i).transitionSymbol;
                writer.write(dfa.getDFA().get(i).startState.stateID + 
                    " -> " + 
                    dfa.getDFA().get(i).endState.stateID + 
                    " [ label = \"" + key + "\" ];\n");
            }

            writer.write("\n}");
            writer.close();
        } catch (IOException e) {
            System.out.println("Something blew up.");
            e.printStackTrace();
        }
    }//output for DFA

    //builds set of transitions in DFA
    private void findNodes(DFA dfa) {
        transitions = new HashSet<Transition>();
        
        for (int i=0; i<dfa.getDFA().size(); i++) {
            if (!dfa.getDFA().get(i).isFiller) {
                transitions.add(dfa.getDFA().get(i));
            }
        }
    }//findNodes for DFA

    //----------------------------------------------------------
}