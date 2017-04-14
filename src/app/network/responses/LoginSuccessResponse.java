package app.network.responses;

import app.App;
import app.game.Game;
import app.game.Player;

/**
 * @author Joël Hoekstra
 */
public class LoginSuccessResponse implements Response {

    private App app;
    private Player player;

    public LoginSuccessResponse(App app, Player player) {
        this.app = app;
        this.player = player;
    }

    @Override
    public void handle() {
        if (app.getGame() == null) {
            return;
        }
        app.addUser(player);
        app.getGame().update();
    }
}
