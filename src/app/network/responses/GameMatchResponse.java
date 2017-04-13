package app.network.responses;

import app.utils.Config;
import app.game.Game;
import app.game.Player;

import java.util.Map;

/**
 * @author Joël Hoekstra
 */
public class GameMatchResponse implements Response {

    private Game game;
    private Map<String, String> params;

    public GameMatchResponse(Game game, Map<String, String> params) {
        this.game = game;
        this.params = params;
    }
    @Override
    public void handle() {
        Player opponent = new Player(params.get("OPPONENT"));
        Player loggedInPlayer = game.getLoggedInPlayer();
        Player playerOne;
        Player playerTwo;
        if (params.get("PLAYERTOMOVE").equals(loggedInPlayer.getUsername())) {
            playerOne = loggedInPlayer;
            playerTwo = opponent;
        } else {
            playerOne = opponent;
            playerTwo = loggedInPlayer;
        }
        game.startMatch(
                playerOne,
                playerTwo,
                Config.getGameTypeFromName(params.get("GAMETYPE")));
    }
}
