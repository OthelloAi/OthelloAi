package app.gui.alerts;

import app.Game;
import javafx.scene.control.Alert;

/**
 * Created by Robert on 7-4-2017.
 */
public class YouWonAlert extends Alert {
     public YouWonAlert(){
        // TODO: 7-4-2017 Add player name, score and reason for win
        super(AlertType.INFORMATION);
        setTitle("Winners message");
        setHeaderText("Congratulations, you won the game!");
        setContentText("You're the winner!");
    }
}
