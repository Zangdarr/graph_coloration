package main;

import exceptions.EdgeAlreadyExistException;
import exceptions.VertexAlreadyExistException;
import exceptions.VertexNotFoundException;
import graphe.Graphe;

import java.io.IOException;

import tools.Coloration;
import tools.ColorationTools;
import tools.GraphTools;

public class DSATMain {

    public static void main(String[] args) {
        callDSAT("1000x70.gph");
    }


    public static void callDSAT(String graphFileName){
        System.out.println("DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  ");
        Graphe g = new Graphe();
        try {
            g = GraphTools.fileToGraph(graphFileName);
        } catch (NumberFormatException | IOException
                | VertexAlreadyExistException | VertexNotFoundException
                | EdgeAlreadyExistException e2) {
            System.out.println(e2.getMessage());
        }
        Coloration result;
        result = ColorationTools.DSATColoration(g);
        System.out.println("Initialisation, " + g.toStringMinimal() + "\n\nResult of DSAT coloration, " + result.toStringMinimal());

        System.out.println("DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  DSAT  ");

    }
    
    public static Coloration callDSAT(Graphe g){
    
        return ColorationTools.DSATColoration(g);

    }
}
