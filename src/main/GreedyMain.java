package main;

import exceptions.EdgeAlreadyExistException;
import exceptions.VertexAlreadyExistException;
import exceptions.VertexNotFoundException;
import graphe.Graphe;

import java.io.IOException;

import tools.Coloration;
import tools.ColorationTools;
import tools.GraphTools;

public class GreedyMain {

    public static void main(String[] args) {
        callGreedy("1000x70.gph");
    }


    public static void callGreedy(String graphFileName){
        System.out.println("GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  ");
        Graphe g = new Graphe();
        try {
            g = GraphTools.fileToGraph(graphFileName);
        } catch (NumberFormatException | IOException
                | VertexAlreadyExistException | VertexNotFoundException
                | EdgeAlreadyExistException e2) {
            System.out.println(e2.getMessage());
        }
        Coloration result;
        result = ColorationTools.greedyColoration(g);
        System.out.println("Initialisation, " + g.toStringMinimal() + "\n\nResult of Greedy coloration, " + result.toStringMinimal());

        System.out.println("GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  GREEDY  ");

    }
    
    public static Coloration callGreedy(Graphe g){
       
        return ColorationTools.greedyColoration(g);


    }
}
