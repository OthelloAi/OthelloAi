package app.responses;

import app.Game;
import app.Player;

import java.util.Map;

/**
 * Created by Martijn Snijder on 7-4-2017.
 */
public class GameDrawResponse implements Response {
    private Map<String, String> params;
    private Game game;

    public GameDrawResponse(Game game, Map<String, String> params) {
        this.game = game;
        this.params = params;
    }

    @Override
    public void handle() {
        Player opponent = new Player(params.get("OPPONENT"));
        Player loggedInPlayer = game.getLoggedInPlayer();
        // TODO: 7-4-2017 Add player score and reason for draw

        game.showNotification(game.getLoggedInPlayer().getUsername() + ", it's a draw!");
    }
}