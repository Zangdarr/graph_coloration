package graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import exceptions.EdgeAlreadyExistException;
import exceptions.VertexAlreadyExistException;
import exceptions.VertexNotFoundException;


public class Graphe implements GrapheInt {

    //ATTRIBUTS
    private HashMap<Integer,Vertex> list_vertex;
    private ArrayList<Edge> list_edges;
    private int nextEdgeID;
    
    
    //CONSTRUCTEURS
    
    /**
     * Initialize the edges list and the vertex list
     */
    public Graphe() {
        super();
        this.list_edges  = new ArrayList<Edge>();
        this.list_vertex = new HashMap<Integer,Vertex>();
        this.nextEdgeID = 1;
    }

    /**
     * @param list_edge : liste des arêtes du graphe
     */
    public Graphe(ArrayList<Edge> list_Edges, HashMap<Integer,Vertex> list_Vertex) {
        super();
        this.list_edges  = list_Edges;
        this.list_vertex = list_Vertex;
    }
    
    
    
    //METHODES

    @Override
    public void addVertex() {
        int key = 0;
        while(list_vertex.containsKey(key))
            key++;
        list_vertex.put(key, new Vertex(key,"" + key));
    }

    @Override
    public void addVertexNumber(int i) throws VertexAlreadyExistException {
        //if the vertex already exist
        if(list_vertex.containsKey(i))
            throw new VertexAlreadyExistException();
        
        //add this vertex
        this.list_vertex.put(i,new Vertex(i,"" + i));
    }

    /**
     * Ajoute une arête au graphe. Si les sommets liée à cette arête n'existe pas, ils seront crées.
     * @param edge
     * @throws VertexNotFoundException
     * @throws EdgeAlreadyExistException
     */
    public void addEdge(Edge edge) throws VertexNotFoundException, EdgeAlreadyExistException{
        Vertex v1 = edge.getVertex_1();
        Vertex v2 = edge.getVertex_2();

        if(!list_vertex.containsKey(v1.getId()))
            list_vertex.put(v1.getId(), v1);
        if(!list_vertex.containsKey(v2.getId()))
            list_vertex.put(v2.getId(), v2);
        
        addEdgeSURE(v1, v2, edge.getPoids());
    }
    
    @Override
    public void addEdge(Vertex v1, Vertex v2,int p) throws VertexNotFoundException, EdgeAlreadyExistException {
        //If the vertex does not exist
        if(! (list_vertex.containsKey(v1.getId()) && list_vertex.containsKey(v2.getId())))
            throw new VertexNotFoundException();
        
        //key calculator
        boolean isV1V2 = v1.getId() < v2.getId();
        //String key_str = (isV1V2)? v1.getString() + "9" + v2.getString() + "99" + v2.getString() : v2.getString() + "9" + v1.getString() + "99" + v1.getString();
        //int key_int = Integer.parseInt(key_str);
        int key_int = nextEdgeID++;
        //if the key already exist into the list
        if(edgeContains(key_int))
            throw new EdgeAlreadyExistException();
        
        //add this edge
        if(isV1V2)
            this.list_edges.add(new Edge(v1,v2, p,key_int));
        else
            this.list_edges.add(new Edge(v2,v1, p,key_int));

    }

    public void addEdgeSURE(int i, int j, int p) throws VertexNotFoundException, EdgeAlreadyExistException {
        boolean isIJ = i<j;
        int key_int = nextEdgeID++;
        //add this edge
        if(isIJ)
            this.list_edges.add(new Edge(list_vertex.get(i), list_vertex.get(j), p,key_int ));
        else
            this.list_edges.add(new Edge(list_vertex.get(j), list_vertex.get(i), p,key_int ));

    }
    public void addEdgeSURE(Vertex v1, Vertex v2, int p) throws VertexNotFoundException, EdgeAlreadyExistException {
        boolean isIJ = v1.getId()<v2.getId();
        int key_int = nextEdgeID++;
        //add this edge
        if(isIJ)
            this.list_edges.add(new Edge(v1, v2, p,key_int ));
        else
            this.list_edges.add(new Edge(v2, v1, p,key_int ));

    }
    
    @Override
    public void addEdge(int i, int j, int p) throws VertexNotFoundException, EdgeAlreadyExistException {
        //If the vertex does not exist
        if(! (list_vertex.containsKey(i) && list_vertex.containsKey(j)))
            throw new VertexNotFoundException();

        //key calculator
        boolean isIJ = i<j;
        //String key_str = (isIJ)? i+"9"+j+"99" + j : j+"9"+i+"99"+i;
        //int key_int = Integer.parseInt(key_str);
        int key_int = nextEdgeID++;
        
        //if the key already exist into the list
        if(edgeContains(key_int))
            throw new EdgeAlreadyExistException("Erreur : L'arête entre ces deux sommets existe déjà : " + "v1 :" + i + " v2 : " + j);
        
        //add this edge
        if(isIJ)
            this.list_edges.add(new Edge(list_vertex.get(i), list_vertex.get(j), p,key_int ));
        else
            this.list_edges.add(new Edge(list_vertex.get(j), list_vertex.get(i), p,key_int ));

    }

    @Override
    public Vertex getVertex(int i) {
        return list_vertex.get(i);
    }

    @Override
    public Iterator<Edge> getSortedEdgeIterator() {
        Collections.sort(list_edges, new EdgesComparator());
        return list_edges.iterator();
    }
    
    public boolean edgeContains(int id){
        for (Iterator<Edge> it = list_edges.iterator(); it.hasNext();)
            if(((Edge) it.next()).getId() == id)
                return true;
        return false;
    }
    
    public boolean vertexContains(int id){
        return list_vertex.containsKey(id);
    }
    
    public int getVertexQuantity(){
        return list_vertex.size();
    }
    
    public int getEdgeQuantity(){
        return list_edges.size();
    }
    
    public double getPoids(){
        double p = 0;
        for (Iterator<Edge> iterator = list_edges.iterator(); iterator.hasNext();) {
            Edge edge = (Edge) iterator.next();
            p += edge.getPoids();
        }
        return p;
    }
    
    public String toStringMinimal(){
        String result = "Graphe :\n - poids : " + getPoids() + "\n - nombre de sommet : " + getVertexQuantity() + "\n - nombre d'arête : " + getEdgeQuantity();
        return result;
    }
    
    public String toString(){
        
        String result = "Graphe :\n - poids : " + getPoids() + "\n - Liste des sommets : \n";
        for(Integer i : list_vertex.keySet()){
            result += i + " ";
        }
        
        result += "\n - Liste des arêtes :\n";
        for(Edge i : list_edges){
            result += i.getVertex_1().getString() + " " + i.getVertex_2().getString() + " " + i.getPoids() + "\n";
        }
        
        return result;
        
    }

    public Iterator<Edge> getSortedEdgeIteratorByFirstVertex() {
        Collections.sort(list_edges, new FirstVertexComparator());
        return list_edges.iterator();
        
    }
    public Iterator<Edge> getEdgeIterator() {
        return list_edges.iterator();
    }
    
    public ArrayList<Edge> getEdgeList(){
        return this.list_edges;
    }

    public Set<Integer> getVertexKeySet() {
        return this.list_vertex.keySet();
    }
    
}

class EdgesComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge a, Edge b) {
        return a.getPoids() < b.getPoids() ? -1 : a.getPoids() == b.getPoids() ? 0 : 1;         
    }
}

class FirstVertexComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge a, Edge b) {
        return a.getVertex_1().getId() < b.getVertex_1().getId() ? -1 : a.getVertex_1().getId() == b.getVertex_1().getId() ? 0 : 1;         
    }
}