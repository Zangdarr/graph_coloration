package main;

import exceptions.EdgeAlreadyExistException;
import exceptions.VertexAlreadyExistException;
import exceptions.VertexNotFoundException;
import graphe.Graphe;

import java.io.IOException;

import tools.Coloration;
import tools.ColorationTools;
import tools.GraphTools;

public class WelshPowellMain {

    public static void main(String[] args) {
        callWelshPowell("10x100.gph");
    }
    
    public static void callWelshPowell(String graphFileName){
        System.out.println("WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  ");
        Graphe g = new Graphe();
        try {
            g = GraphTools.fileToGraph(graphFileName);
        } catch (NumberFormatException | IOException
                | VertexAlreadyExistException | VertexNotFoundException
                | EdgeAlreadyExistException e2) {
            System.out.println(e2.getMessage());
        }
        Coloration result;
        result = ColorationTools.WelshPowellColoration(g);
        System.out.println("Initialisation, " + g.toStringMinimal() + "\n\nResult of WelshPowell coloration, " + result.toStringMinimal());

        System.out.println("WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  WelshPowell  ");

    }
    
    
}
