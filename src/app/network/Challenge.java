package app.network;

import app.game.GameType;

/**
 * @author Joël Hoekstra
 */
public class Challenge {

    private int id = -1;
    private String username;
    private GameType gameType;


    public Challenge(String username, GameType gameType) {
        this.username = username;
        this.gameType = gameType;
    }

    public String getChallenger() {
        return username;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
