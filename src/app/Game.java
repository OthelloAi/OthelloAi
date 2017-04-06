package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import app.commands.*;
import app.gui.GUI;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Joël Hoekstra
 */
public class Game extends Application implements Protocol {
    private CommandSender sender;
    private Stage stage;
    private GameType gameType = GameType.TIC_TAC_TOE;
    private ArrayList<Player> playerList;
    private Random rand;
    private GUI gui;
    private boolean loggedIn = false;
    private Board board;
    private Player loggedInPlayer;

    private boolean inMatch = false;
    private Match match;

    public Game() {
        rand = new Random();
        board = new Board(gameType);
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

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public Token[][] getBoard() {
        return board.getBoard();
    }

    public void setPlayers(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        sender = new CommandSender(this);
        Thread thread = new Thread(sender);
        thread.setDaemon(true);
        thread.start();

        gui = new GUI(this);
        Scene scene = new Scene(gui, 1024, 768);
        stage.setTitle("Two player game");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        stage.close();
    }

    public void setLogin(boolean loggedIn) {
        this.loggedIn = loggedIn;
        gui.update();
        gui.render();
    }

    public void showAlert(String message) {
        showAlert(message, "", "");
    }

    public void showAlert(String message, String title, String header) {
        Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR: " + title);
                alert.setHeaderText(header);
                alert.setContentText(message);
                alert.showAndWait();
            });
    }

    public void handleCommand(Command command) {
//        System.out.println(command);
        sender.addCommand(command);
    }

    public boolean isLoggedIn() {
        return (loggedInPlayer != null);
    }

    public void startMatch(Player playerOne, Player playerTwo, GameType gameType) {
        if (match == null) {
            match = new Match(gameType, playerOne, playerTwo);
        }
    }

    public Move getNextMove() {
        return new Move(0, getLoggedInPlayer());
    }

    public void placeMove(Move move) {
        if (match != null) {
            match.addMove(move);
            board.addMove(move.getPosition(), getTokenByPlayer(move.getPlayer()));
            gui.update();
        }
    }

    private Token getTokenByPlayer(Player player) {
        if (match != null) {
            System.out.println("token for player: " + player.getUsername());
            if (player.getUsername().equals(match.getPlayerOne().getUsername())) {
                return new Token('X');
            }

            if (player.getUsername().equals(match.getPlayerTwo().getUsername())) {
                return new Token('O');
            }
        }
        return new Token('-');
    }
}
