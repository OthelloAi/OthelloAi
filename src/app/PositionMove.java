package app;

/**
 * @author Joël Hoekstra
 */
public class PositionMove {
    public Position position;
    public Token token;
    public Token oldToken;

    public PositionMove(Position position, Token token) {
        this(position, token, token);
    }

    public PositionMove(Position position, Token token, Token oldToken) {
        this.token = token;
        this.oldToken = oldToken;
        this.position = position;
    }
}
