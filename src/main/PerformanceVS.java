package main;

import exceptions.EdgeAlreadyExistException;
import exceptions.VertexAlreadyExistException;
import exceptions.VertexNotFoundException;
import graphe.Graphe;

import java.io.IOException;

import tools.Coloration;
import tools.GraphTools;

public class PerformanceVS {

    public static void main(String[] args) {
        callFight("10x70.gph");
    }
        
        
        
        
        
        
    public static void callFight(String graphFileName){
        System.out.println("VS  VS  VS  VS  VS  VS  VS  VS  VS  VS  ");
        Graphe g = new Graphe();
        try {
            g = GraphTools.fileToGraph(graphFileName);
        } catch (NumberFormatException | IOException
                | VertexAlreadyExistException | VertexNotFoundException
                | EdgeAlreadyExistException e2) {
            System.out.println(e2.getMessage());
        }
        
        Coloration GREEDYresult = GreedyMain.callGreedy(g),
                   WPresult     = WelshPowellMain.callWelshPowell(g),
                   DSATresult   = DSATMain.callDSAT(g);
        
        System.out.println(toString(GREEDYresult,WPresult,DSATresult));
        

    }






    private static String toString(Coloration gREEDYresult,
            Coloration wPresult, Coloration dSATresult) {
            
            StringBuffer result = new StringBuffer("[[  PERFORMANCES  ]] \n\n");
            
            result.append("---  EXECUTION TIME  ---\n");
            result.append(" - Greedy       coloration time : ");
            result.append(gREEDYresult.getExecutionTime() + "\n");
            result.append(" - Welsh-Powell coloration time : ");
            result.append(wPresult.getExecutionTime() + "\n");
            result.append(" - DSAT         coloration time : ");
            result.append(dSATresult.getExecutionTime() + "\n");
            
            result.append("\n---  Coloration  ---\n");
            result.append(" - Greedy       colors quantity : ");
            result.append(gREEDYresult.getNbColors() + "\n");
            result.append(" - Welsh-Powell colors quantity : ");
            result.append(wPresult.getNbColors() + "\n");
            result.append(" - DSAT         colors quantity : ");
            result.append(dSATresult.getNbColors() + "\n");
            
            

            
        
        return result.toString();
    }

}
