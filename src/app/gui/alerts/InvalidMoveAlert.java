package app.gui.alerts;

import javafx.scene.control.Alert;

/**
 * Created by Martijn Snijder on 15-4-2017.
 */
public class InvalidMoveAlert extends Alert {
    public InvalidMoveAlert() {
        super(Alert.AlertType.INFORMATION);
        setTitle("Invalid move");
        setHeaderText("Sorry, this is not a valid move");
        setContentText("Please try again");
    }
}