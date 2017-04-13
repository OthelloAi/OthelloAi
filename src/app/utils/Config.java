package app.utils;

import app.game.GameType;

/**
 * @author Joël Hoekstra
 */
public final class Config {
    private Config() {
        // do nothing
    }

    public static final String getGameTypeName(GameType gameType) {
        String name = "";
        if (gameType == GameType.REVERSI) {
            name = "Reversi";
        }
        if (gameType == GameType.TIC_TAC_TOE) {
            name = "Tic-tac-toe";
        }
        return name;
    }

    public static final GameType getGameTypeFromName(String name) {
        if (name.equals("Reversi")) {
            return GameType.REVERSI;
        }

        if (name.equals("Tic-tac-toe")) {
            return GameType.TIC_TAC_TOE;
        }

        return null;
    }
}
