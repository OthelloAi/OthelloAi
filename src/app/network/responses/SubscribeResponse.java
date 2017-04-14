package app.network.responses;

import app.App;
import app.game.Game;
import app.Subscribe;

/**
 * @author Joël Hoekstra
 */
public class SubscribeResponse implements Response {
    private App app;
    private Subscribe subscribe;

    public SubscribeResponse(App app, Subscribe subscribe) {
        this.app = app;
        this.subscribe = subscribe;
    }

    @Override
    public void handle() {
        app.getGame().setGameType(subscribe.getGameType());
    }
}
