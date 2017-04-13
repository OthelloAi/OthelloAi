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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Pair;
import app.network.commands.*;
import java.util.Optional;
import java.util.ArrayList;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.application.Platform;

/**
 * @author Joël Hoekstra
 */
public class GUI extends BorderPane {
    private Game game;
    private App app;
    private GameGUI gameGUI;
    Integer movePosition;
    Label scoreLabel;// = "Your score: 0, Opponents score: 0";

    public GUI(App app) {
        this.app = app;
        this.game = app.getGame();
        game.addGUI(this);

        setTop(getLabels());
        setBottom(getButtons());
        render();
    }

    public void render() {
        if (game.isLoggedIn()) {

//            System.out.println("board: " + game.getBoard());
            Platform.runLater(() -> {
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
            });
        }
    }

    private HBox getLabels() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(
                getScoreLabel()
        );
        return hBox;
    }

    private HBox getButtons() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(
                getLoginButton(),
                getSubscribeButton(),
                getChallengeButton(),
                getPlayerListButton(),
                getForfeitButton(),
                getAllPlayersButton(),
                getMoveButton(),
                getAiButton(),
                getLogoutButton()
        );
        return hBox;
    }

    private Button getAiButton() {
        Button btn = new Button("AI");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> {
            int position = game.getActor().getNext(game.getPossibleMoves());
            Move move = new Move(position, game.getLoggedInPlayer());
            CommandSender.addCommand(new MoveCommand(move));
        });
        return btn;
    }

    private Button getMoveButton() {
        Button btn = new Button("Move");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> {
            MoveDialog dialog = new MoveDialog();
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(movePosition -> {
                this.movePosition = Integer.parseInt(movePosition);
                System.out.println(this.movePosition);
                game.handleMove(this.movePosition);
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
                CommandSender.addCommand(new ChallengeCommand(command.getKey(), Config.getGameTypeFromName(command.getValue())));
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
            result.ifPresent(gameType -> CommandSender.addCommand(new SubscribeCommand(Config.getGameTypeFromName(gameType))));

        });
        return btn;
    }

    private Button getLoginButton() {
        Button btn = new Button("Login");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> {
            LoginDialog dialog = new LoginDialog();
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(command -> CommandSender.addCommand(new LoginCommand(result.get())));
            scoreLabel.setText("Your score: 0              vs               Opponents score: 0"); // TODO: 13/04/2017 refactor to someplace else 
        });

        return btn;
    }

    private Button getLogoutButton() {
        Button btn = new Button("Logout");
        btn.setPrefSize(80, 30);
        btn.setOnAction((ActionEvent e) -> {
            CommandSender.addCommand(new LogoutCommand());
            try {
                app.stop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        return btn;
    }

    private Button getAllPlayersButton() {
        Button btn = new Button("Show all players");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e-> {List<String> showPlayers = new ArrayList<>();

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
            CommandSender.addCommand(new PlayerListCommand());});
        return btn;
    }

    private Button getPlayerListButton() {
        Button btn = new Button("Get Playerlist");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> CommandSender.addCommand(new PlayerListCommand()));
        return btn;
    }
  
    private Button getPendingChallenges(){
        Button btn = new Button("Get pending challenges");
        btn.setPrefSize(80,30);
        btn.setOnAction(e-> {
            ArrayList<Challenge> challenges = game.getPendingChallenges();

        });
        return btn;
    }

    private Label getScoreLabel(){
        scoreLabel = new Label();
        scoreLabel.setText("You're score: ");
        return scoreLabel;
    }

    private Button getGameListButton() {
        Button btn = new Button("games");
        btn.setPrefSize(80, 30);
        btn.setOnAction(e -> CommandSender.addCommand(new GameListCommand()));
        return btn;
    }

    private Button getForfeitButton(){
        Button btn = new Button("Forfeit");
        btn.setPrefSize(80,30);
        btn.setOnAction(e -> CommandSender.addCommand(new ForfeitCommand()));
        return btn;
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
