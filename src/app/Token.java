package app;

/**
 * @author Joël Hoekstra
 */
public class Token {
    private char token;

    public Token(char token) {
        this.token = token;
    }

    public String toString() {
        return "" + token;
    }
}
