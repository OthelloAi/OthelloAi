package app.network.responses;

import app.App;
import app.Challenge;
import app.game.GameType;
import app.utils.Config;
import app.game.Game;
import app.utils.Debug;

import java.util.Map;

/**
 * @author Joël Hoekstra
 */
public class ChallengeResponse implements Response {

    private App app;
    private Map<String, String> params;
    private Challenge challenge;

    public ChallengeResponse(App app, Map<String, String> params) {
        this.params = params;
        this.app = app;
        this.challenge = new Challenge(
                params.get("CHALLENGER"),
                Config.getGameTypeFromName(params.get("GAMETYPE"))
        );
        this.challenge.setId(Integer.parseInt(params.get("CHALLENGENUMBER")));
    }
    
    @Override
    public void handle() {
        app.getGame().addPendingChallenge(challenge);
        Debug.println("challenge has been send");
    }
}
