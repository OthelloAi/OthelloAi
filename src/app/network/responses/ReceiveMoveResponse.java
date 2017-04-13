package app.network.responses;

import app.game.Game;
import app.game.Move;
import app.game.Player;

import java.util.Map;

/**
 * @author Joël Hoekstra
 */
public class ReceiveMoveResponse implements Response {

    private Game game;
    private Map<String, String> params;

    public ReceiveMoveResponse(Game game, Map<String, String> params) {
        this.game = game;
        this.params = params;
    }

    @Override
    public void handle() {
        // PLAYER
        // MOVE
        // DETAILS
        if (!params.get("DETAILS").equals("Illegal move")) {
            Move move = new Move(
                    Integer.parseInt(params.get("MOVE")),
                    new Player(params.get("PLAYER"))
            );
            game.placeMove(move);
        }
    }
}
