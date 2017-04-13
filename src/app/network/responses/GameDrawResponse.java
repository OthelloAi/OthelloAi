package app.network.responses;

import app.game.Game;
import app.gui.alerts.DrawGameAlert;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.Map;

/**
 * Created by Martijn Snijder on 7-4-2017.
 */
public class GameDrawResponse implements Response {
    private Map<String, String> params;
    private Game game;

    public GameDrawResponse(Game game, Map<String, String> params) {
        this.game = game;
        this.params = params;
    }

    @Override
    public void handle() {
        Platform.runLater(() -> {
            Alert alert = new DrawGameAlert();
            alert.showAndWait();
        });
    }
}