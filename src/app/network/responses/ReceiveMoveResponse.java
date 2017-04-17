package app.network.responses;

import app.App;
import app.Token;
import app.game.Game;
import app.game.Move;
import app.game.Player;

import java.util.Map;

/**
 * @author Joël Hoekstra
 */
public class ReceiveMoveResponse implements Response {

    private App app;
    private Map<String, String> params;

    public ReceiveMoveResponse(App app, Map<String, String> params) {
        this.app = app;
        this.params = params;
    }

    @Override
    public void handle() {
        // PLAYER
        // MOVE
        // DETAILS
        if (!params.get("DETAILS").equals("Illegal move")) {
            Player player;
            if (app.getGame().getMatch().getPlayerTwo().getUsername().equals(params.get("PLAYER"))) {
                player = app.getGame().getMatch().getPlayerTwo();
            } else {
                player = app.getGame().getMatch().getPlayerOne();
            }
            Move move = new Move(
                    Integer.parseInt(params.get("MOVE")),
                    player
            );
            app.getGame().placeMove(move);
        }
    }
}
