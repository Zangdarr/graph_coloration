package exceptions;

public class GrapheException extends Exception {

    /**
     * 
     */
    public GrapheException() {
        super("ERROR : Problème survenue lors d'une manipulation de graphe.");
    }

    /**
     * @param message
     */
    public GrapheException(String message) {
        super(message);
    }
}
