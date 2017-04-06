package app.responses;

import app.Game;
import app.Move;
import app.Player;

import java.util.Map;

/**
 * @author js
 */
public class MoveResponse implements Response {

    private Map<String, String> params;
    private Game game;

    public MoveResponse(Game game, Map<String, String> params) {
        this.game = game;
        this.params = params;
    }

    @Override
    public void handle() {
        int position = Integer.parseInt(params.get("MOVE"));
        Player player = new Player(params.get("PLAYER"));
        game.placeMove(new Move(position, player));
//        System.out.println("Makes a move " + position);
//        game.makeMove(new Move(position));
    }
}
