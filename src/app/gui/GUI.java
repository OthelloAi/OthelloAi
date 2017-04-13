package app.gui;

import app.*;

import java.util.List;

import app.game.Game;
import app.game.GameType;
import app.game.Move;
import app.game.Player;
import app.gui.dialogs.*;
import app.network.CommandSender;
import app.utils.Config;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import app.network.commands.*;
import java.util.Optional;
import java.util.ArrayList;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.application.Platform;

/**
 * @author Joël Hoekstra
 */
public class GUI extends BorderPane {

    private MenuBar menuBar;
    private Game game;
    private App app;
    private GameGUI gameGUI;
    private Integer movePosition;

    private Label leftStatus = new Label("Try to login. See File > Login");
    private Label rightStatus = new Label("");

    public GUI(App app) {
        this.app = app;
        this.game = app.getGame();
        game.addGUI(this);
        render();
    }

    private void createMenuBar() {
        menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        menuBar.getMenus().add(menuFile);
        if (game.isLoggedIn()) {
            menuFile.getItems().addAll(
                    menuItemSubscribe(),
                    menuItemAllPlayers(),
                    menuItemChallenge(),
                    menuItemLogout()
            );
            if (game.isInMatch()) {
                menuBar.getMenus().addAll(
                        menuActions(),
                        menuAI()
                );
            }
        } else {
            menuFile.getItems().add(menuItemLogin());
        }
        setTop(menuBar);
    }

    public void render() {
        Platform.runLater(() -> {
            createMenuBar();
            createStatusBar();
            if (game.isLoggedIn()) {
                if (game.getGameType() == GameType.REVERSI && !(gameGUI instanceof OthelloGUI) && game.isInMatch()) {
                    gameGUI = new OthelloGUI(game.getBoard());
                    setCenter(gameGUI);
                }
                if (game.getGameType() == GameType.TIC_TAC_TOE && !(gameGUI instanceof TicTacToeGUI) && game.isInMatch()) {
                    gameGUI = new TicTacToeGUI(game.getBoard());
                    setCenter(gameGUI);
                }
                if (gameGUI != null) {
                    gameGUI.render();
                }
            }
        });
    }

    private void createStatusBar() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(leftStatus, rightStatus);
        setBottom(hBox);
    }

    private Menu menuActions() {
        Menu menu = new Menu("Actions");
        menu.getItems().addAll(
                menuItemForfeit(),
                menuItemMove()
        );
        return menu;
    }

    private Menu menuAI() {
        Menu menu = new Menu("AI");
        menu.getItems().add(
                menuItemMoveAI()
        );
        return menu;
    }

    private MenuItem menuItemLogin() {
        MenuItem item = new MenuItem("Login");
        item.setOnAction(e -> {
            LoginDialog dialog = new LoginDialog();
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(command -> {
                CommandSender.addCommand(new LoginCommand(result.get()));
                leftStatus.setText("Welcome " + command);
            });
        });
        return item;
    }

    private MenuItem menuItemAllPlayers() {
        MenuItem item = new MenuItem("Show Players");
        item.setOnAction(e-> {List<String> showPlayers = new ArrayList<>();

            Stage stage = new Stage();

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
            Scene scene = new Scene(grid, 300, 300);
            stage.setScene(scene);
            stage.setTitle("All players");
            stage.show();

            ListView lv = new ListView();
            Platform.runLater(() -> {
                for (Player player : game.getPlayerList()) {
                    lv.getItems().add(player.getUsername());
                }
            });

            grid.add(lv,0,0);
            Button refreshBtn = new Button();
            refreshBtn.setPrefSize(80,30);
            refreshBtn.setText("Refresh");
            refreshBtn.setOnAction(ee-> {
                lv.getItems().clear();
                CommandSender.addCommand(new PlayerListCommand());
                try{
                    Thread.sleep(100);
                }catch (InterruptedException we){}
                for (Player player : game.getPlayerList()) {
                    lv.getItems().add(player.getUsername());
                }
            });

            grid.add(refreshBtn,0,1);
            game.addStage(stage);
            CommandSender.addCommand(new PlayerListCommand());
        });
        return item;
    }

    private MenuItem menuItemMove() {
        MenuItem item = new MenuItem("Move");
        item.setOnAction(e -> {
            MoveDialog dialog = new MoveDialog();
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(movePosition -> {
                this.movePosition = Integer.parseInt(movePosition);
                game.handleMove(this.movePosition);
            });
        });
        return item;
    }

    private MenuItem menuItemMoveAI() {
        MenuItem item = new MenuItem("Do Move");
        item.setOnAction(e -> {
            int position = game.getActor().getNext(game.getPossibleMoves());
            Move move = new Move(position, game.getLoggedInPlayer());
            CommandSender.addCommand(new MoveCommand(move));
        });
        return item;
    }

    private MenuItem menuItemForfeit() {
        MenuItem item = new MenuItem("Forfeit");
        item.setOnAction(e -> {
            CommandSender.addCommand(new ForfeitCommand());
        });
        return item;
    }

    private MenuItem menuItemChallenge() {
        MenuItem item = new MenuItem("Challenge Player");
        item.setOnAction(e -> {
            ChallengeDialog dialog = new ChallengeDialog(game);
            Optional<Pair<String, String>> result = dialog.display();
            result.ifPresent(command -> {
                CommandSender.addCommand(new ChallengeCommand(command.getKey(), Config.getGameTypeFromName(command.getValue())));
            });
        });
        return item;
    }
    private MenuItem menuItemSubscribe() {
        MenuItem item = new MenuItem("Subscribe");
        item.setOnAction(e -> {
            List<String> choices = new ArrayList<>();
            choices.add("Reversi");
            choices.add("Tic-tac-toe");
            SubscribeDialog<String> dialog = new SubscribeDialog<>("Reversi", choices);
            leftStatus.setText("Subscribing...");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(gameType -> {
                leftStatus.setText("Subscribed to " + gameType + ". Waiting for opponent..");
                CommandSender.addCommand(new SubscribeCommand(Config.getGameTypeFromName(gameType)));
            });
        });
        return item;
    }

    private MenuItem menuItemLogout() {
        MenuItem item = new MenuItem("Logout");
        item.setOnAction(e -> app.stop());
        return item;
    }

    public void reset() {
        gameGUI = null;
        update();
    }

    public void update() {
        // grab everything if something has changed
        render();
    }
}
