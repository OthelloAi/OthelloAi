package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import app.commands.*;
import app.gui.GUI;

import java.util.Random;

/**
 * @author Joël Hoekstra
 */
public class Game extends Application implements Protocol {
    private CommandSender sender;
    private Stage stage;
    private GameType gameType = GameType.TIC_TAC_TOE;

    private Random rand;
    private GUI gui;
    private boolean loggedIn = false;

    public Game() {
        rand = new Random();
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
        gui.render();
    }

    public Token[][] getBoard() {
        Token token;
        Token[][] board = null;
        if (gameType == GameType.TIC_TAC_TOE) {
            board = new Token[3][3];
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (rand.nextInt(20) >= 10) {
                        token = new Token('X');
                    } else {
                        token = new Token('O');
                    }
                    board[y][x] = token;
                }
            }
        }

        if (gameType == GameType.REVERSI) {
            board = new Token[8][8];
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    if (rand.nextInt(20) >= 10) {
                        token = new Token('X');
                    } else {
                        token = new Token('O');
                    }
                    board[y][x] = token;
                }
            }
        }
        return board;
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
        System.out.println(command);
        sender.addCommand(command);
    }

    public boolean isLoggedIn() {
        return true; //loggedIn;
    }
}
