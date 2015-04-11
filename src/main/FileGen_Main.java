package main;

import java.io.IOException;

import tools.GraphTools;
import graphe.Graphe;
import graphe.RandomGraphGenerator;

public class FileGen_Main {

    public static void main(String[] args) {
        RandomGraphGenerator gen = new RandomGraphGenerator();
        System.out.println("START");
        Graphe g = gen.generateErdosRenyiGraph(10,(float) 1.0);
        try {
            GraphTools.graphToFile("10x100.gph", g);
        } catch (IOException e) {
            System.err.println("Erreur lors de la création d'un fichier graphe. " + e.getMessage());
        }
    }

}
