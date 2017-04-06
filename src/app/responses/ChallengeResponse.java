package app.responses;

import app.Game;

import java.util.Map;

/**
 * @author js
 */
public class ChallengeResponse implements Response {

    private Game game;

    public ChallengeResponse(Game game, Map<String, String> params) {
        this.game = game;
    }

    @Override
    public void handle() {
        game.showNotification(game.getLoggedInPlayer().getUsername() + " you have a new challenge!");
//        game.showDialog(new ChallengeAcceptDialog(game, params));
        System.out.println("challenge has been send");
    }
}
