package app;

/**
 * @author Joël Hoekstra
 */
public class Subscribe {
    private GameType gameType;

    public Subscribe(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }
}
