package app.network.responses;

import app.Challenge;
import app.game.GameType;
import app.utils.Config;
import app.game.Game;

import java.util.Map;

/**
 * @author Joël Hoekstra
 */
public class ChallengeResponse implements Response {

    private Game game;
    private Map<String, String> params;
    private Challenge challenge;

    public ChallengeResponse(Game game, Map<String, String> params) {
        this.params = params;
        this.game = game;
        this.challenge = new Challenge(
                params.get("CHALLENGER"),
                Config.getGameTypeFromName(params.get("GAMETYPE"))
        );
        this.challenge.setId(Integer.parseInt(params.get("CHALLENGENUMBER")));
    }

    public ChallengeResponse(Game game, String challenger, GameType gameType, int id) {
        this.game = game;
        this.challenge = new Challenge(challenger, gameType);
    }
    
    @Override
    public void handle() {
//        Challenge challenge = new Challenge(params.get("CHALLENGER"), Config.getGameTypeFromName(params.get("GAMETYPE")));
//        challenge.setId(Integer.parseInt(params.get("CHALLENGENUMBER")));
        game.addPendingChallenge(challenge);
        System.out.println("challenge has been send");
    }
}
