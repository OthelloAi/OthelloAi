package app.network.responses;

import app.App;
import app.game.Game;
import app.gui.alerts.ErrorAlert;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Created by Gabe on 13-04-17.
 */
public class ErrorResponse implements Response {
    private App app;
    private String responseString;

    public ErrorResponse(App app, String responseString) {
        this.app = app;
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
