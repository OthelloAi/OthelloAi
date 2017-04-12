package app.gui.alerts;

import javafx.scene.control.Alert;

/**
 * Created by Robert on 7-4-2017.
 */
public class DrawGameAlert extends Alert {
    public DrawGameAlert(){
        // TODO: 7-4-2017 Add player name, score and reason for draw
        super(AlertType.INFORMATION);
        setTitle("No winner!");
        setHeaderText("It's a draw, no winner!");
        setContentText("Draw game");
    }
}
