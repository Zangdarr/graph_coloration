package exceptions;

public class VertexNotFoundException extends Exception {

    /**
     * 
     */
    public VertexNotFoundException() {
        super("ERROR : Le vertex n'a pas été trouvé.");
    }

    /**
     * @param message
     */
    public VertexNotFoundException(String message) {
        super(message);
    }

    
}
