package app.gui.alerts;

import javafx.scene.control.Alert;

/**
 * Created by Robert on 7-4-2017.
 */
public class YouLostAlert extends Alert {
    public YouLostAlert(){
        super(AlertType.INFORMATION);
        setTitle("Losers message");
        setHeaderText("Sorry, you lost the game!");
        setContentText("You've lost the game!");
    }
}
