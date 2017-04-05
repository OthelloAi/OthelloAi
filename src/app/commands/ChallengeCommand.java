package app.commands;

import app.Config;
import app.GameType;

/**
 * @author js
 */
public class ChallengeCommand implements Command {

    private String username;
    private GameType gameType;

    public ChallengeCommand(String username, GameType gameType) {
        this.username = username;
        this.gameType = gameType;
    }

    @Override
    public String toString() {
        return "challenge \"" + username + "\" \"" + Config.getGameTypeName(gameType) + "\" ";
    }
}
