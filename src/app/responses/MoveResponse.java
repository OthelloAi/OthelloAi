package app.responses;

import app.Game;

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
        int position = Integer.getInteger(params.get("MOVE"));
//        game.makeMove(new Move(position));
    }
}
