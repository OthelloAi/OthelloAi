package app.commands;

import app.Config;
import app.GameType;

/**
 * @author js
 */
public class SubscribeCommand implements Command {
    private GameType gameType;

    public SubscribeCommand(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public String toString() {
        return "subscribe " + Config.getGameTypeName(gameType);
    }

}
