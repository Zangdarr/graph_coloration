package main;

import java.io.IOException;

import tools.GraphTools;
import graphe.Graphe;
import graphe.RandomGraphGenerator;

public class FileGen_Main {

    public static void main(String[] args) {

        //CONFIGURATION
        int nbSommet = 1000;
        float proba = (float) 1.0;
        String filename = nbSommet +"x"+ (int)(proba*100) +".gph";



        RandomGraphGenerator gen = new RandomGraphGenerator();
        System.out.println("START");
        Graphe g = gen.generateErdosRenyiGraph(nbSommet,proba);
        try {
            GraphTools.graphToFile(filename, g);
        } catch (IOException e) {
            System.err.println("Erreur lors de la création d'un fichier graphe. " + e.getMessage());
        }

    }

}
