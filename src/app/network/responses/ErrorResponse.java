package app.network.responses;

import app.game.Game;
import app.gui.alerts.ErrorAlert;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Created by Gabe on 13-04-17.
 */
public class ErrorResponse implements Response{
    private Game game;
    private String responseString;

    public ErrorResponse(Game game, String responseString) {
        this.game = game;
        this.responseString = responseString;
    }

    @Override
    public void handle() {
        Platform.runLater(() -> {
            Alert alert = new ErrorAlert();
            alert.setContentText(responseString);
            alert.showAndWait();
        });
    }
}
