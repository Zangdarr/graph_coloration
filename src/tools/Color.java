package tools;

public class Color {
    private int id;
    private String name;

    /**
     * @param id
     */
    public Color(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        return ((Color)obj).getId() == this.id;
    }
    
    
    
}
