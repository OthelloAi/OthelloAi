package app.gui.dialogs;

import app.App;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

/**
 * @author Joël Hoekstra
 */
public class ChallengePlayerDialog extends Dialog {
    private App app;

    private String player;

    public ChallengePlayerDialog(App app, String player) {
        this.app = app;
        this.player = player;
    }

    public Optional<Pair<String, String>> display() {
        Dialog<Pair<String, String>> dialog = new Dialog();
        dialog.setTitle("Challenge player " + player);

        // set the button types
        ButtonType challengeButtonType = new ButtonType("Challenge", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(challengeButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Reversi", "Tic-tac-toe");
        comboBox.getSelectionModel().selectFirst();

        grid.add(new Label("Username:"), 0, 0);
        grid.add(new Label(player), 1, 0);
        grid.add(new Label("Game:"), 0, 2);
        grid.add(comboBox, 1, 2);

        dialog.getDialogPane().setContent(grid);


        // Convert the result to a username-game-pair when the challenge button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == challengeButtonType) {
                return new Pair<>(player, comboBox.getSelectionModel().getSelectedItem().toString());
            }
            return null;
        });
        return dialog.showAndWait();
    }
}
