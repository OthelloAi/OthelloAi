package app.network.responses;

import app.App;
import app.Token;
import app.game.Game;
import app.game.Move;
import app.game.Player;
import app.utils.Debug;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Joël Hoekstra
 */
public class ReceiveMoveResponse implements Response {

    private App app;
    private Map<String, String> params;
    private final CountDownLatch receiveMoveLatch = new CountDownLatch(1);

    private static final int REQUEST_TIMEOUT_SECS = 1;

    public ReceiveMoveResponse(App app, Map<String, String> params) {
        this.app = app;
        this.params = params;

        Debug.println("receivemoveresponse " + params.get("PLAYER") + " " + params.get("MOVE"));
    }

    @Override
    public void handle() {
        if (!params.get("DETAILS").equals("Illegal move")) {
            Player player;
            if (app.getGame().getMatch().getPlayerTwo().getUsername().equals(params.get("PLAYER"))) {
                player = app.getGame().getMatch().getPlayerTwo();
            } else {
                player = app.getGame().getMatch().getPlayerOne();
            }
            Move move = new Move(
                    Integer.parseInt(params.get("MOVE")),
                    player
            );
            System.out.println("RECEIVED move " + move.getPosition() + " from player " + player.getUsername() + " with token " + player.getToken());
            app.getGame().processMove(move);
        }
    }
}