package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import exceptions.EdgeAlreadyExistException;
import exceptions.VertexAlreadyExistException;
import exceptions.VertexNotFoundException;
import graphe.Edge;
import graphe.Graphe;

public class GraphTools {

    /**
     * Produit un graphe à partir d'un fichier
     * @param filename : nom du fichier
     * @return
     * @throws IOException
     * @throws NumberFormatException : si le fichier ne contient pas que des nombres avec des espace entre eux
     * @throws VertexAlreadyExistException : si un vertex est placé deux fois en première colonne.
     * @throws VertexNotFoundException
     * @throws EdgeAlreadyExistException : si deux arête identique sont écrite dans le fichier.
     */
    public static Graphe fileToGraph(String filename) throws IOException, NumberFormatException, VertexAlreadyExistException, VertexNotFoundException, EdgeAlreadyExistException{
        Graphe tmp = new Graphe();
        System.out.println("From file " + filename + " to graphe..." );
        BufferedReader buff = new BufferedReader(new FileReader(filename));
        String line = "";
        String[] tab;

        while ((line = buff.readLine()) != null) {
            tab = line.split(" ");
            int vertex_1 = Integer.parseInt(tab[0]),
                vertex_2 = -1;
            if(!tmp.vertexContains(vertex_1))
                tmp.addVertexNumber(vertex_1);
            for (int i = 1; i < tab.length-1; i+=2) {
                vertex_2 = Integer.parseInt(tab[i]);
                if(!tmp.vertexContains(vertex_2))
                    tmp.addVertexNumber(vertex_2);
                tmp.addEdgeSURE(tmp.getVertex(vertex_1), tmp.getVertex(vertex_2), Integer.parseInt(tab[i+1]));
            }
            
        }
        
        buff.close();
        
        System.out.println("done.");
        return tmp;
    }


    
    public static void graphToFile(String filename, Graphe g) throws IOException {
        System.out.println("From graphe to file" + filename +"..." );

        Iterator<Edge> it = g.getSortedEdgeIteratorByFirstVertex();
        
        StringBuffer graphe_txt = new StringBuffer("1");
        
        //iterator trié donc le premier sommet est le numéro 1
        int currentFirst = 1,
            nextID = -1;
        
        for (Iterator<Edge> iterator = it; iterator.hasNext();) {
            Edge edge = iterator.next();
            nextID = edge.getVertex_1().getId();
            if(nextID != currentFirst){
                graphe_txt.append('\n');
                graphe_txt.append(nextID);
                currentFirst = nextID;
            }
            graphe_txt.append(" " + edge.getVertex_2().getId() + " " + edge.getPoids());
        }
        
        PrintWriter p = new PrintWriter(new FileWriter(filename));
        
        p.write(graphe_txt.toString());
        p.close();
        System.out.println("done.");
    }
}
