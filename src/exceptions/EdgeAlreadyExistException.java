package exceptions;

public class EdgeAlreadyExistException extends Exception {

    /**
     * 
     */
    public EdgeAlreadyExistException() {
        super("ERROR : L'arête existe déjà.");
    }

    /**
     * @param message
     */
    public EdgeAlreadyExistException(String message) {
        super(message);
    }

    
}
