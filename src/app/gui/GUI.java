package app.gui;

import app.*;

import java.awt.*;
import java.util.List;

import app.game.Game;
import app.game.GameType;
import app.game.Move;
import app.game.Player;
import app.gui.dialogs.*;
import app.network.CommandSender;
import app.utils.Config;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
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
 * @author Robert Zandberg
 */
public class GUI extends BorderPane {

    private MenuBar menuBar;
    private Game game;
    private App app;
    private GameGUI gameGUI;
    private Label leftStatus = new Label("Try to login. See File > Login");

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
                    gameGUI.setOnMouseClicked(e -> handleMouseClick(e));
                    setCenter(gameGUI);
                }
                if (game.getGameType() == GameType.TIC_TAC_TOE && !(gameGUI instanceof TicTacToeGUI) && game.isInMatch()) {
                    gameGUI = new TicTacToeGUI(game.getBoard());
                    gameGUI.setOnMouseClicked(e -> handleMouseClick(e));
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
        hBox.getChildren().addAll(leftStatus);
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
                setLeftStatusText("Welcome " + command);
            });
        });
        return item;
    }

    public void setLeftStatusText(String text) {
        Platform.runLater(() -> leftStatus.setText(text));
    }

    private MenuItem menuItemAllPlayers() {
        MenuItem item = new MenuItem("Show Players");
        item.setOnAction(e -> {
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
                for (Player player : app.getOnlinePlayers()) {
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
                for (Player player : app.getOnlinePlayers()) {
                    lv.getItems().add(player.getUsername());
                }
            });

            lv.setOnMouseClicked(lve -> {
                String possibleOpponent = lv.getSelectionModel().getSelectedItem().toString();
                ChallengePlayerDialog dialog = new ChallengePlayerDialog(app, possibleOpponent);
                Optional<Pair<String, String>> result = dialog.display();
                setLeftStatusText("Challenging player " + lv.getSelectionModel().getSelectedItem().toString());
                result.ifPresent(command -> {
                    setLeftStatusText("Challenged " + command.getKey() + " for a game of " + command.getValue());
                    CommandSender.addCommand(new ChallengeCommand(command.getKey(), Config.getGameTypeFromName(command.getValue())));
                });
            });

            grid.add(refreshBtn,0,1);
            app.addStage(stage);
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
                setLeftStatusText("Made the move " + movePosition);
                game.handleMove(Integer.parseInt(movePosition));
            });
        });
        return item;
    }

    private MenuItem menuItemMoveAI() {
        MenuItem item = new MenuItem("Do Move");
        item.setOnAction(e -> {
            int position = game.getActor().getNext(game.getPossibleMoves());
            Move move = new Move(position, app.getUser());
            CommandSender.addCommand(new MoveCommand(move));
        });
        return item;
    }

    private MenuItem menuItemForfeit() {
        MenuItem item = new MenuItem("Forfeit");
        item.setOnAction(e -> {
            setLeftStatusText("You have forfeited the game");
            CommandSender.addCommand(new ForfeitCommand());
            game.forfeit();
        });
        return item;
    }

    private MenuItem menuItemChallenge() {
        MenuItem item = new MenuItem("Challenge Player");
        item.setOnAction(e -> {
            ChallengeDialog dialog = new ChallengeDialog(app);
            Optional<Pair<String, String>> result = dialog.display();
            setLeftStatusText("Challenging another player");
            result.ifPresent(command -> {
                setLeftStatusText("Challenged " + command.getKey() + " for a game of " + command.getValue());
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
            setLeftStatusText("Subscribing...");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(gameType -> {
                setLeftStatusText("Subscribed to " + gameType + ". Waiting for opponent..");
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

    private void handleMouseClick(MouseEvent e){
        Double doubleX = e.getX();
        Double doubleY = e.getY();
        int intX = doubleX.intValue();
        int intY = doubleY.intValue();
        int posX = (intX / 80);
        int posY = (intY / 80);
        int position = (posY * 8) + posX;
        game.handleMove(position);
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
