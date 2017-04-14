package app.network.responses;

import app.App;
import app.utils.Config;
import app.game.Game;
import app.game.Player;

import java.util.Map;

/**
 * @author Joël Hoekstra
 */
public class GameMatchResponse implements Response {

    private App app;
    private Map<String, String> params;

    public GameMatchResponse(App app, Map<String, String> params) {
        this.app = app;
        this.params = params;
    }
    @Override
    public void handle() {
        Player opponent = new Player(params.get("OPPONENT"));
        Player loggedInPlayer = app.getUser();
        Player playerOne;
        Player playerTwo;
        if (params.get("PLAYERTOMOVE").equals(loggedInPlayer.getUsername())) {
            playerOne = loggedInPlayer;
            playerTwo = opponent;
        } else {
            playerOne = opponent;
            playerTwo = loggedInPlayer;
        }
        app.getGame().startMatch(
                playerOne,
                playerTwo,
                Config.getGameTypeFromName(params.get("GAMETYPE")));

    }
}
