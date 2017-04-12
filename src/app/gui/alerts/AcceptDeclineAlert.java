package app.gui.alerts;


import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;

/**
 * Created by Robert on 11-4-2017.
 */
public class AcceptDeclineAlert extends Alert{

    public AcceptDeclineAlert(){
        super(AlertType.CONFIRMATION);
        setTitle("Invite challenge");
        setHeaderText("Accept / Decline");
        setContentText("Accept or Decline your challenge:");
    }


}
