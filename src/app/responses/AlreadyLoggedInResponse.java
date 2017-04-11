package app.responses;

import app.Game;
import app.gui.alerts.AlreadyLoggedInAlert;
import app.gui.alerts.YouWonAlert;
import javafx.application.Platform;
import javafx.scene.control.Alert;

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
        Platform.runLater(() -> {
            Alert alert = new AlreadyLoggedInAlert();
            alert.showAndWait();
        });
    }
}
