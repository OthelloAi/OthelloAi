package app.gui.dialogs;

import javafx.scene.control.TextInputDialog;

/**
 * @author Joël Hoekstra
 */
public class LoginDialog extends TextInputDialog {
    public LoginDialog() {
        super("username");

        this.setTitle("Login Dialog");
        this.setHeaderText("Login to server");
        this.setContentText("Please enter your username:");
    }
}
