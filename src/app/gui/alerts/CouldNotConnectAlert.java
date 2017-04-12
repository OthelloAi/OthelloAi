package app.gui.alerts;

import javafx.scene.control.Alert;

/**
 * @author Gabe Witteveen
 */
public class CouldNotConnectAlert extends Alert {
    public CouldNotConnectAlert() {
        super(AlertType.ERROR);
        setTitle("Error: Could not connect to server");
        setHeaderText("Error: Could not connect to server");
        setContentText("Could not connect to server");
    }
}