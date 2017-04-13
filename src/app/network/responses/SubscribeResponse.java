package app.network.responses;

import app.game.Game;
import app.Subscribe;

/**
 * @author Joël Hoekstra
 */
public class SubscribeResponse implements Response {
    private Game game;
    private Subscribe subscribe;

    public SubscribeResponse(Game game, Subscribe subscribe) {
        this.game = game;
        this.subscribe = subscribe;
    }

    @Override
    public void handle() {
        game.setGameType(subscribe.getGameType());
    }
}
