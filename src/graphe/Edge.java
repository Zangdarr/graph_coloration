package graphe;

public class Edge {
    
    //ATTRIBUTS
    
    private Vertex vertex_1,
                   vertex_2;
    private int    poids,
                   id;
    
    
    //CONSTRUCTEUR
    
    /**
     * @param vertex_1
     * @param vertex_2
     */
    public Edge(Vertex vertex_1, Vertex vertex_2, int poids, int id) {
        super();
        this.vertex_1 = vertex_1;
        this.vertex_2 = vertex_2;
        this.poids    = poids;
        this.id = id;
    }
    
    
    //GETTER
    
    public Vertex getVertex_1() {
        return vertex_1;
    }
    public Vertex getVertex_2() {
        return vertex_2;
    }
    public int getPoids() {
        return poids;
    }
    public int getId() {
        return id;
    }
    
    
    public String toString(){
        return "Edge id,  p : Edge " + id + " "+ vertex_1.getId() + " " + vertex_2.getId() + " "+ poids;
    }
    
}
