package app.gui.alerts;

import javafx.scene.control.Alert;

/**
 * Created by Robert on 15-4-2017.
 */
// TODO: 15-4-2017 Implement this alert 
public class NotYourTurnAlert extends Alert {
    public NotYourTurnAlert() {
        super(Alert.AlertType.INFORMATION);
        setTitle("Not your turn");
        setHeaderText("Sorry, it's not your turn");
        setContentText("Please wait until your opponent made a move");
    }
}