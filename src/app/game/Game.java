package app.game;

import app.*;
import app.actors.Actor;
import app.actors.MiniMaxActor;
import app.gui.alerts.CouldNotConnectAlert;
import app.gui.dialogs.ConnectionDialog;
import app.gui.alerts.AcceptDeclineAlert;
import app.gui.alerts.StartMatchAlert;
import app.network.CommandSender;
import app.network.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import app.network.commands.*;
import app.gui.GUI;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joël Hoekstra
 */
public class Game {// implements Protocol {
//    private CommandSender sender;
//    private Random rand;
    private GUI gui;
    private boolean loggedIn;
    private Board board;
    private GameType gameType;
    private Player loggedInPlayer;
    private ArrayList<Player> playerList;
    private ArrayList<Challenge> pendingChallenges;
    private ArrayList<Stage> stages = new ArrayList<>();

    private Actor actor;
    private Match match = null;

    public Game() {
        pendingChallenges = new ArrayList<>();
//        rand = new Random();
        loggedIn = false;
        board = new Board(gameType);
        actor = new MiniMaxActor(this, board);
    }

    public void addGUI(GUI gui) {
        this.gui = gui;
    }

    public Actor getActor() {
        return actor;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
        board = new Board(gameType);
        gui.render();
    }

    public void setLoggedInPlayer(Player player) {
        loggedInPlayer = player;
    }

    public Player getLoggedInPlayer() {
        return loggedInPlayer;
    }

    public boolean isInMatch() {
        return (match != null && match.isStarted() && !match.isFinished());
    }

    public ArrayList<Player> getPlayerList() {return playerList;}


    public Token[][] getBoard() {
        return board.getBoard();
    }

    public void setPlayers(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPendingChallenge(Challenge challenge) {
        this.pendingChallenges.add(challenge);
        //TODO With @Martijn alerts toevoegen.
        // show dialog here
        Platform.runLater(() -> {
            AcceptDeclineAlert dialog = new AcceptDeclineAlert();
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == ButtonType.OK) {
                CommandSender.addCommand(new ChallengeAcceptCommand(challenge));

            } else {
                this.pendingChallenges.remove(challenge);
            }
        });
    }



    public ArrayList<Challenge> getPendingChallenges() {return pendingChallenges;}

    public void addStage(Stage stage) {
        stages.add(stage);
    }

    public void setLogin(boolean loggedIn) {
        this.loggedIn = loggedIn;
        update();
    }

    public ArrayList<Integer> getPossibleMoves() {
        return board.getPossibleMoves();
    }
  
//    public void showNotification(String message) {
//        showNotification(message, "", "");
//    }
//
//    public void showNotification(String message, String title, String header) {
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Notification: " + title);
//            alert.setHeaderText(header);
//            alert.setContentText(message);
//            alert.showAndWait();
//        });
//    }
//
//    public void showAlert(String message) {
//        showAlert(message, "", "");
//    }
//
//    public void showAlert(String message, String title, String header) {
//        Platform.runLater(() -> {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("ERROR: " + title);
//                alert.setHeaderText(header);
//                alert.setContentText(message);
//                alert.showAndWait();
//            });
//    }
//    public void handleCommand(Command command) {
//        sender.addCommand(command);
//    }

    public boolean isLoggedIn() {
        return (loggedInPlayer != null);
    }

    public void startMatch(Player playerOne, Player playerTwo, GameType gameType) {
        System.out.println("You're placed in a new match..");
        Platform.runLater(() -> {
            StartMatchAlert startMatchAlert = new StartMatchAlert();
            startMatchAlert.showAndWait();
        });
        board = new Board(gameType);
        gui.reset();
        match = new Match(gameType, playerOne, playerTwo);
    }

    public void placeMove(Move move) {
        if (isLoggedIn()) {
            if (isInMatch()) {
                match.addMove(move);
                board.addMove(move.getPosition(), getTokenByPlayer(move.getPlayer()));
                if (gameType == GameType.REVERSI) {
                    board.flipColors(move, getTokenByPlayer(move.getPlayer()));
                }
                gui.update();
            }
        }
        else {
            // perhaps the reset should happen here.
            // or an empty game gui should be rendered.
        }
    }

    public void handleMove(Integer movePosition){
        Move move = new Move(movePosition, getLoggedInPlayer());
        if(board.isValidMove(move, getTokenByPlayer(move.getPlayer())))
        {
            CommandSender.addCommand(new MoveCommand(move));
            System.out.println("Nice one, valid move");
        }
        else
            {
                // return alert that move isn't valid
                System.out.println("Invalid move!");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Invalid move");
                    alert.setContentText("Sorry, the move you chose isn't valid");
                    alert.showAndWait();
                });
            }
    }

    private Token getTokenByPlayer(Player player) {
        if (match != null) {
            System.out.println("Token for player: " + player.getUsername());
            if (player.getUsername().equals(match.getPlayerOne().getUsername())) {
                if (gameType == GameType.REVERSI) return new Token(TokenState.BLACK);
                if (gameType == GameType.TIC_TAC_TOE) return new Token(TokenState.CROSS);
            }

            if (player.getUsername().equals(match.getPlayerTwo().getUsername())) {
                if (gameType == GameType.REVERSI) return new Token(TokenState.WHITE);
                if (gameType == GameType.TIC_TAC_TOE) return new Token(TokenState.NOUGHT);
            }
        }
        return new Token(TokenState.EMPTY);
    }

    public void update() {
        gui.update();
    }
}
