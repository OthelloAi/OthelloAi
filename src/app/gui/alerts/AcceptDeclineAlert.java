package app.gui.alerts;


import app.Challenge;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Created by Robert on 11-4-2017.
 */
public class AcceptDeclineAlert extends Alert{
    String gameType;
    String challenger;

    public AcceptDeclineAlert(Challenge challenge){
        super(AlertType.CONFIRMATION);
        Challenge inviteChallenge = challenge;
        challenger = inviteChallenge.getChallenger().toString();
        if (inviteChallenge.getGameType().toString().equals("REVERSI"))
            gameType = "Reversi";
        else
            gameType = "Tic-Tac-Toe";
        setTitle("New invite");
        setHeaderText(challenger + " invited you for a game of " + gameType + "!");
        setContentText("Please accept or decline this challenge");
        Button okButton = (Button) getDialogPane().lookupButton( ButtonType.OK );
        okButton.setText("Accept");
        Button cancelButton = (Button) getDialogPane().lookupButton( ButtonType.CANCEL );
        cancelButton.setText("Decline");
    }


}
