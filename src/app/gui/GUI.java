package app.gui;

import app.*;

import java.util.List;

import app.gui.dialogs.MoveDialog;
import javafx.util.Pair;
import app.commands.*;
import java.util.Optional;
import java.util.ArrayList;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.application.Platform;
import app.gui.dialogs.LoginDialog;
import app.gui.dialogs.ChallengeDialog;
import app.gui.dialogs.SubscribeDialog;

/**
 * @author Joël Hoekstra
 */
public class GUI extends BorderPane {
    private Game game;
    private GameGUI gameGUI;

    public GUI(Game game) {
        this.game = game;
        setBottom(getButtons());
        render();
    }


    public void render() {
        if (game.isLoggedIn()) {
            System.out.println("board: " + game.getBoard());
            Platform.runLater(() -> {
                if (game.getGameType() == GameType.REVERSI && !(gameGUI instanceof OthelloGUI)) {
                    gameGUI = new OthelloGUI(game.getBoard());
                    setCenter(gameGUI);
                }
                if (game.getGameType() == GameType.TIC_TAC_TOE && !(gameGUI instanceof TicTacToeGUI)) {
                    gameGUI = new TicTacToeGUI(game.getBoard());
                    setCenter(gameGUI);
                }
                gameGUI.render();
            });
        }
    }

    private HBox getButtons() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(
                getLoginButton(),
                getSubscribeButton(),
                getChallengeButton(),
                getPlayerListButton(),
                getAllPlayersButton(),
//                getGameListButton(),
                getMoveButton(),
                getLogoutButton()
        );
        return hBox;
    }

    private Button getMoveButton() {
        Button btn = new Button("Move");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> {
            MoveDialog dialog = new MoveDialog();
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(movePosition -> {
                System.out.println(movePosition);
                if (game.isLoggedIn()) {
                    if (game.isInMatch()) {
                        Move move = new Move(Integer.parseInt(movePosition), game.getLoggedInPlayer());
                        game.handleCommand(new MoveCommand(move));
                    }
                }
//                System.out.println("Move: " + move.getPosition() + " - " + move.getPlayer());
//                game.handleCommand(new MoveCommand(new Move(Integer.getInteger(movePosition), game.getLoggedInPlayer())));
            });
        });
        return btn;
    }

    private Button getChallengeButton() {
        Button btn = new Button("Challenge");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> {
            ChallengeDialog dialog = new ChallengeDialog(game);
            Optional<Pair<String, String>> result = dialog.display();
            result.ifPresent(command -> {
                game.handleCommand(new ChallengeCommand(command.getKey(), Config.getGameTypeFromName(command.getValue())));
            });
        });
        return btn;
    }

    private Button getSubscribeButton() {
        Button btn = new Button("Subscribe");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> {
            List<String> choices = new ArrayList<>();
            choices.add("Reversi");
            choices.add("Tic-tac-toe");
            SubscribeDialog<String> dialog = new SubscribeDialog<>("Reversi", choices);

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(gameType -> game.handleCommand(new SubscribeCommand(Config.getGameTypeFromName(gameType))));

        });
        return btn;
    }

    private Button getLoginButton() {
        Button btn = new Button("Login");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> {
            LoginDialog dialog = new LoginDialog();
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(command -> game.handleCommand(new LoginCommand(result.get())));
        });
        return btn;
    }

    private Button getLogoutButton() {
        Button btn = new Button("Logout");
        btn.setPrefSize(80, 30);
        btn.setOnAction((ActionEvent e) -> {
            game.handleCommand(new LogoutCommand());
            try {
                game.stop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        return btn;
    }

    private Button getAllPlayersButton() {
        Button btn = new Button("Show all players");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e->game.handleCommand(new PlayerListCommand()));
        return btn;
    }

    private Button getPlayerListButton() {
        Button btn = new Button("Get Playerlist");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> game.handleCommand(new PlayerListCommand()));
        return btn;
    }

    // van uit game via list opslaan en dan oproepen
    private Button getPendingChallenges(){
        Button btn = new Button("Get pending challenges");
        btn.setPrefSize(80,30);
        btn.setOnAction(e-> {
            ArrayList<Challenge> challenges = game.getPendingChallenges();

        });
        return btn;
    }

    private Button getGameListButton() {
        Button btn = new Button("games");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> game.handleCommand(new GameListCommand()));
        return btn;
    }


    public void update() {
        // grab everything if something has changed
        render();
    }
}
