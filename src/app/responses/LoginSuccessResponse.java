package app.responses;

import app.Game;
import app.Player;

/**
 * @author Joël Hoekstra
 */
public class LoginSuccessResponse implements Response {

    private Game game;
    private Player player;

    public LoginSuccessResponse(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void handle() {
        if (game == null) {
            return;
        }
        game.setLogin(true);
        game.setLoggedInPlayer(player);
    }
}
