package app.game;

import app.utils.Token;

/**
 * @author Joël Hoekstra
 */
public class Player {

    private String username;
    private Token token;
    private Player opponent;

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
}
