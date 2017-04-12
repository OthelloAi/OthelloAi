package app.responses;

import app.Challenge;
import app.Config;
import app.Game;

import java.util.Map;

/**
 * @author js
 */
public class ChallengeResponse implements Response {

    private Game game;
    private Map<String, String> params;
    public ChallengeResponse(Game game, Map<String, String> params) {
        this.params = params;
        this.game = game;
    }
    
    @Override
    public void handle() {
        Challenge challenge = new Challenge(params.get("CHALLENGER"), Config.getGameTypeFromName(params.get("GAMETYPE")));
        challenge.setId(Integer.parseInt(params.get("CHALLENGENUMBER")));
        game.addPendingChallenge(challenge);


//        game.showDialog(new ChallengeAcceptDialog(game, params));
        System.out.println("challenge has been send");
    }
}
