package app;

/**
 * @author Joël Hoekstra
 */
public class Token {
    private TokenState tokenState;

    public Token(TokenState tokenState) {
        this.tokenState = tokenState;
    }

    @Override
    public String toString() {
        return "" + tokenState.symbol();//String.valueOf(tokenState.symbol());
    }
}
