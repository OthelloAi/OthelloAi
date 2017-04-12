package app.gui.alerts;


import javafx.scene.control.Alert;

/**
 * Created by Robert on 12-4-2017.
 */
public class StartMatchAlert extends Alert {
    public StartMatchAlert(){
        super(AlertType.INFORMATION);
        setTitle("Game accepted!");
        setHeaderText("Good luck");
        setContentText("You're placed in a match. Good luck");
    }
}
