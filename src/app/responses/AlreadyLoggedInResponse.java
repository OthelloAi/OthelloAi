package app.responses;

import app.Game;

/**
 * @author Joël Hoekstra
 */
public class AlreadyLoggedInResponse implements Response {

    private Game game;

    public AlreadyLoggedInResponse(Game game) {
        this.game = game;
    }

    @Override
    public void handle() {
        String message = "Oops you are already logged in";
        game.showAlert(
                message,
                "Already Logged In",
                "Login Error");
    }
}
