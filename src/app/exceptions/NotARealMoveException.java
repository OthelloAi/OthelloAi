package app.exceptions;

/**
 * @author Joël Hoekstra
 */
// Checked Exception example from http://stackoverflow.com/a/1754473
public class NotARealMoveException extends Exception {
    public NotARealMoveException() { super(); }
    public NotARealMoveException(String message) { super(message); }
    public NotARealMoveException(String message, Throwable cause) { super(message, cause); }
    public NotARealMoveException(Throwable cause) { super(cause); }
}