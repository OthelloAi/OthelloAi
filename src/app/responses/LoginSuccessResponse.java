package app.responses;

import app.Game;

/**
 * @author Joël Hoekstra
 */
public class LoginSuccessResponse implements Response {

    private Game game;

    public LoginSuccessResponse(Game game) {
        this.game = game;
    }

    @Override
    public void handle() {
        game.setLogin(true);
    }
}
