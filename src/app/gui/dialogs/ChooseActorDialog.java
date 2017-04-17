package app.gui.dialogs;

import javafx.scene.control.ChoiceDialog;
import java.util.List;

/**
 * @author Gabe Witteveen
 */
public class ChooseActorDialog<T> extends ChoiceDialog {
    public ChooseActorDialog(String defaultChoice, List<String> choices) {
        super(defaultChoice, choices);


        this.setTitle("Choose Actor");
        this.setHeaderText("Choose which algorithm is used");
        this.setContentText("Please pick an algorithm");
    }
}
