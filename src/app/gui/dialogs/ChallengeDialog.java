package app.gui.dialogs;

import app.App;
import app.game.Game;
import app.game.Player;
import app.network.CommandSender;
import app.network.commands.PlayerListCommand;
import app.utils.Debug;
import javafx.geometry.Insets;
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
    private App app;
    public ChallengeDialog(App app) {
        this.app = app;
    }

    public Optional<Pair<String, String>> display() {
        CommandSender.addCommand(new PlayerListCommand());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Challenge Dialog");

        // Create the username and gametype labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Set the button types.
        ButtonType challengeButtonType = new ButtonType("Challenge", ButtonBar.ButtonData.OK_DONE);

        ComboBox<String> userbox = new ComboBox<>();
        ArrayList<Player> playerList = app.getOnlinePlayers();

        if (playerList != null) { // catch an none existing arraylist
            for (Player player : playerList) {
                if (!player.getUsername().equals(app.getUser().getUsername())) {
                    userbox.getItems().add(player.getUsername());
                }
            }
            if (userbox.getItems().toString().equals("[]")) {
                grid.add(new Label("You are the only online player!"), 1, 3);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            }
            else{
                dialog.getDialogPane().getButtonTypes().addAll(challengeButtonType, ButtonType.CANCEL);
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
