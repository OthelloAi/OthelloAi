package app.network.responses;

import app.App;
import app.game.GameState;
import app.gui.alerts.YouLostAlert;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.Map;

/**
 * Created by Martijn Snijder on 7-4-2017.
 */
public class GameLossResponse implements Response {
    private Map<String, String> params;
    private App app;

    public GameLossResponse(App app, Map<String, String> params) {
        this.app = app;
        this.params = params;
    }

    @Override
    public void handle() {
        app.getGame().endMatch(GameState.LOSS);
        Platform.runLater(() -> {
            Alert alert = new YouLostAlert();
            alert.showAndWait();
        });
    }
}