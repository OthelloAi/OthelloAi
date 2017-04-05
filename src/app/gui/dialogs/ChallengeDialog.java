package app.gui.dialogs;

import app.Game;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Reversi", "Tic-tac-toe");
        comboBox.getSelectionModel().selectFirst();

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Game:"), 0, 2);
        grid.add(comboBox, 1, 2);

        // Enable/Disable login button depending on whether a username was entered.
        Node challengeButton = dialog.getDialogPane().lookupButton(challengeButtonType);
        challengeButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            challengeButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-game-pair when the challenge button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == challengeButtonType) {
                return new Pair<>(username.getText(), comboBox.getSelectionModel().getSelectedItem().toString());
            }
            return null;
        });
        return dialog.showAndWait();
    }
}
