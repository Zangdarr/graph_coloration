package graphe;

public class Vertex {

    //ATTRIBUTS
    
    private int id;
    private String name;
    
    //CONSTRUCTOR
    
    /**
     * @param id   : vertex id
     * @param name : vertex name
     */
    public Vertex(int id,String name) {
        super();
        this.id = id;
        this.name = name;
    }

    //GETTER

    public String getString() {
        return name;
    }

    public int getId() {
        return id;
    }

    

}
