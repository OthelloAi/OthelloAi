package app.gui.dialogs;

import javafx.scene.control.TextInputDialog;

/**
 * @author Joël Hoekstra
 */
public class MoveDialog extends TextInputDialog {
    public MoveDialog() {
        setTitle("Make your move");
        setHeaderText("You can make your move now!");
        setContentText("Please enter your move:");
    }
}
