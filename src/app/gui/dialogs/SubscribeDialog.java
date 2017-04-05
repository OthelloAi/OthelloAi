package app.gui.dialogs;

import javafx.scene.control.ChoiceDialog;

import java.util.List;

/**
 * @author Joël Hoekstra
 */
public class SubscribeDialog<T> extends ChoiceDialog {
    public SubscribeDialog(String defaultChoice, List<String> choices) {
        super(defaultChoice, choices);


        this.setTitle("Subscribe Dialog");
        this.setHeaderText("Subscribe to game");
        this.setContentText("Please choose your game:");
    }
}
