package app.gui.alerts;

import app.responses.AlreadyLoggedInResponse;
import javafx.scene.control.Alert;

/**
 * @author Joël Hoekstra
 */
public class AlreadyLoggedInAlert extends Alert {
    public AlreadyLoggedInAlert() {
        super(AlertType.ERROR);
        setTitle("Error: Already Logged In");
        setHeaderText("Error: Already Logged In");
        setContentText("Oops, you are already logged in");
    }
}
