package app.gui.alerts;

import javafx.scene.control.Alert;

/**
 * @author Gabe Witteveen
 */
public class ErrorAlert extends Alert {
    public ErrorAlert() {
        super(AlertType.ERROR);
        setTitle("Error");
        setHeaderText("Error");
        setContentText("Error");
    }
}