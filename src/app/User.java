package app;

import app.game.GameType;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class User {
    private String username;
    private History history;

    public User() {
//        this.username = username;
        history = new History();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addMatch(Match match) {
        history.addMatch(match);
    }

    public Match getMatch(int m) {
        return history.getMatch(m);
    }
}
