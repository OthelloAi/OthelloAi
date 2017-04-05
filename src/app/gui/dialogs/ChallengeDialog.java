package app.gui.dialogs;

import app.Game;
import app.Player;
import app.commands.PlayerListCommand;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Got the main idea from :http://code.makery.ch/blog/javafx-dialogs-official/
 * @author Joël Hoekstra
 */

public class ChallengeDialog extends Dialog {
    private Game game;
    public ChallengeDialog(Game game) {
        this.game = game;
    }

    public Optional<Pair<String, String>> display() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Challenge Dialog");

        // Set the button types.
        ButtonType challengeButtonType = new ButtonType("Challenge", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(challengeButtonType, ButtonType.CANCEL);

        // Create the username and gametype labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> userbox = new ComboBox<>();
        ArrayList<Player> playerList = game.getPlayerList();

        if (playerList != null) { // catch an none existing arraylist
            for (Player player : playerList) {
                userbox.getItems().add(player.getUsername());
            }
            userbox.getSelectionModel().selectFirst();
        }
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Reversi", "Tic-tac-toe");
        comboBox.getSelectionModel().selectFirst();

        grid.add(new Label("Username:"), 0, 0);
        grid.add(userbox, 1, 0);
        grid.add(new Label("Game:"), 0, 2);
        grid.add(comboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-game-pair when the challenge button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == challengeButtonType) {
                return new Pair<>(userbox.getSelectionModel().getSelectedItem().toString(), comboBox.getSelectionModel().getSelectedItem().toString());
            }
            return null;
        });
        return dialog.showAndWait();
    }
}
