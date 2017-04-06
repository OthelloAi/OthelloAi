package app.responses;

import app.Config;
import app.Game;
import app.Player;

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
        System.out.println(params.get("PLAYERTOMOVE"));
        System.out.println(params.get("OPPONENT"));
        game.startMatch(
                new Player(params.get("PLAYERTOMOVE")),
                new Player(params.get("OPPONENT")),
                Config.getGameTypeFromName(params.get("GAMETYPE")));
    }
}
