package app;

/**
 * @author Joël Hoekstra
 */
public class Token {
    private TokenState tokenState;

    public Token(TokenState tokenState) {
        this.tokenState = tokenState;
    }

    public TokenState getState() {
        return tokenState;
    }

    @Override
    public String toString() {
        return "" + tokenState.symbol();
    }
}
