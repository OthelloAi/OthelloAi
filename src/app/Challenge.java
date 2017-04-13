package app;

import app.game.GameType;

/**
 * @author Joël Hoekstra
 */
public class Challenge {

    private int id = -1;
    private String username;
    private GameType gameType;
    private boolean accepted = false;


    public Challenge(String username, GameType gameType) {
        this.username = username;
        this.gameType = gameType;
    }

    public boolean accept(boolean accept) {
        this.accepted = accept;
        return accept;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
