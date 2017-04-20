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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import app.network.commands.*;
import java.util.Optional;
import java.util.ArrayList;
import javafx.scene.layout.*;
import javafx.application.Platform;

/**
 * @author Joël Hoekstra
 * @author Robert Zandberg
 * @author Martijn Snijder
 * @author Gabe Witteveen
 */
public class GUI extends BorderPane {

    private MenuBar menuBar;
    private Game game;
    private App app;
    private GameGUI gameGUI;
    private Label leftStatus = new Label("Try to login. See File > Login");

    private int boardLength;

    public GUI(App app) {
        this.app = app;
        this.game = app.getGame();
        game.addGUI(this);
        gameGUI = new GameGUI(this);
        render();
    }

    private void createMenuBar() {
        menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        menuBar.getMenus().add(menuFile);
        if (game.isLoggedIn()) {
            menuFile.getItems().addAll(
                    menuItemSubscribe(),
                    menuItemChooseActor(),
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
            createCenter();
        });
    }

    private void createCenter() {
        if (game.isLoggedIn()) {
            if (game.getMatch() != null && game.getMatch().isStarted()) {
                this.boardLength = game.getBoard().length;
                VBox vBox = new VBox();
                vBox.setAlignment(Pos.TOP_CENTER);
                vBox.setSpacing(10);

                Pane scorePane = new Pane();
                scorePane.setPrefWidth(widthProperty().doubleValue());


                String youMsg = "You " + game.getLoggedInPlayer().getToken() + " : " + game.getScore(game.getLoggedInPlayer());
                String oppMsg = "Opponent " + game.getLoggedInPlayer().getOpponent().getToken() + " : " + game.getScore(game.getLoggedInPlayer().getOpponent());

                Text scoreText = new Text(youMsg + " | " + oppMsg);
                scorePane.getChildren().add(scoreText);

                gameGUI = new GameGUI(game.getBoard(), this);
                vBox.getChildren().addAll(scorePane, gameGUI);
                gameGUI.setPrefSize(vBox.getWidth(), vBox.getHeight());
                gameGUI.setBoard(game.getBoard());


                vBox.setMargin(scorePane, new Insets(20, 10, 0, 10));
                vBox.setMargin(gameGUI, new Insets(20, 20,  20, 20));
                setCenter(vBox);
                game.showHelp();
                gameGUI.render();
            }
        }
    }

    private void createStatusBar() {
        HBox hBox = new HBox();
        hBox.getChildren().add(leftStatus);
        setBottom(hBox);
    }

    private Menu menuActions() {
        Menu menu = new Menu("Actions");
        menu.getItems().addAll(
                menuItemForfeit(),
                menuItemMove()
        );
        if (game.getGameType() == GameType.REVERSI)
            menu.getItems().add(menuItemHelp());
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
                if (!possibleOpponent.equals(app.getUser().getUsername())) {
                    ChallengePlayerDialog dialog = new ChallengePlayerDialog(app, possibleOpponent);
                    Optional<Pair<String, String>> result = dialog.display();
                    setLeftStatusText("Challenging player " + lv.getSelectionModel().getSelectedItem().toString());
                    result.ifPresent(command -> {
                        setLeftStatusText("Challenged " + command.getKey() + " for a game of " + command.getValue());
                        CommandSender.addCommand(new ChallengeCommand(command.getKey(), Config.getGameTypeFromName(command.getValue())));
                    });
                }
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
                int position = Integer.parseInt(movePosition);
                Move move = new Move(position, app.getUser());
                game.handleMove(move);
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

    public MenuItem menuItemHelp(){
        final MenuItem item = new MenuItem("Help");
        item.setOnAction(e -> {
            game.setShowingHelp(!game.getShowingHelp());
            game.update();
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

    private MenuItem menuItemChooseActor() {
        MenuItem item = new MenuItem("Choose Actor");
        item.setOnAction(e -> {
            List<String> choices = new ArrayList<>();
            choices.add("MiniMax");
            choices.add("Iterative");
            choices.add("Random");
            choices.add("Human");
            ChooseActorDialog<String> dialog = new ChooseActorDialog<>("MiniMax", choices);
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(Actor -> {
                setLeftStatusText("Actor: " + Actor);
                game.setActorState(Actor);
                if (!Actor.equals("Human")) {
                    game.useAI(true);
                } else {
                    game.useAI(false);
                }
            });
        });
        return item;
    }
  
    public void handleMouseClick(int x, int y){
        if (game.isYourTurn()) {
            Move move = new Move((y * boardLength + x), game.getLoggedInPlayer());
            game.handleMove(move);
        }
    }

    public void reset() {
        gameGUI = null;
        update();
    }

    public void update() {
        render();
    }
}
