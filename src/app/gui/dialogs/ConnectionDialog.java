package app.gui.dialogs;

import app.Protocol;
import javafx.scene.control.TextInputDialog;

/**
 * @author Gabe Witteveen
 */
public class ConnectionDialog extends TextInputDialog {

    public ConnectionDialog() {
        super(Protocol.SERVER_HOST + ":" + Protocol.SERVER_PORT);

        this.setTitle("Connect Dialog");
        this.setHeaderText("Connect to server");
        this.setContentText("Please enter the ip address and port number");
    }
}
