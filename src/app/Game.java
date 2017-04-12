package app;

import app.actors.Actor;
import app.actors.MiniMaxActor;
import app.gui.alerts.CouldNotConnectAlert;
import app.gui.dialogs.ConnectionDialog;
import app.gui.dialogs.LoginDialog;
import app.gui.alerts.AcceptDeclineAlert;
import app.gui.alerts.StartMatchAlert;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import app.commands.*;
import app.gui.GUI;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joël Hoekstra
 */
public class Game extends Application implements Protocol {
    private CommandSender sender;
    private ArrayList<Stage> stages = new ArrayList<>();
    private GameType gameType = GameType.TIC_TAC_TOE;
    private ArrayList<Player> playerList;
    private Random rand;
    private GUI gui;
    private boolean loggedIn = false;
    private Board board;
    private Player loggedInPlayer;
    private ArrayList<Challenge> pendingChallenges = new ArrayList<Challenge>();
    private Thread commandSenderThread = null;
    private String hostName = SERVER_HOST;
    private int portNumber = SERVER_PORT;

    CountDownLatch latch = new CountDownLatch(1);
    private Actor actor;
    private Match match = null;

    public Game() {
        rand = new Random();
        board = new Board(gameType);
        actor = new MiniMaxActor(this, board);
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
                handleCommand(new ChallengeAcceptCommand(challenge));

            } else {
                this.pendingChallenges.remove(challenge);
            }
        });
    }


    public ArrayList<Challenge> getPendingChallenges() {return pendingChallenges;}

    @Override
    public void start(Stage stage) {
        Connection connection = new Connection();
        while (true) {
            if (askConnectionInfo()) {
                connection.connect(hostName, portNumber);
                if (connection.isConnected()) {
                    break;
                } else {
                    Alert alert = new CouldNotConnectAlert();
                    alert.setContentText("Could not connect to server");
                    alert.showAndWait();
                }
            }
        }
        stages.add(stage);
        sender = new CommandSender(this, latch, connection.getSocket());
        commandSenderThread = new Thread(sender);
        commandSenderThread.setDaemon(true);
        commandSenderThread.start();

        askLoginName();

        gui = new GUI(this);
        Scene scene = new Scene(gui, 800, 600);
        stage.setTitle("Two player game");
        stage.setScene(scene);
        stage.setX(0);
        stage.setY(0);
        stage.show();
        stage.setOnCloseRequest(e -> stop());

        try {
            // Wait for CommandSenderThread to connect to server
            if (latch.await(2, TimeUnit.SECONDS)) {
                // Show the GUI
                stage.show();
            } else {
                System.exit(0);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean askConnectionInfo() {

        // Create a new Dialog asking for an ip address and port number
        ConnectionDialog connectionDialog = new ConnectionDialog();
        Optional<String> connectionResult = connectionDialog.showAndWait();

        // Check if the user pressed OK
        if (connectionResult.isPresent()) {
            // Check if connectionResult is a valid ip address and port number
            String str = connectionResult.get();
            Pattern pattern = Pattern.compile("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}:\\d{1,6}|localhost:\\d{1,5}");
            Matcher matcher = pattern.matcher(str);
            // If connectionResult is valid set hostName and portNumber
            if (matcher.find()) {
                hostName = str.substring(0, str.indexOf(":"));
                portNumber = Integer.parseInt(str.substring(str.indexOf(":")+1));
                System.out.println("hostname: " + hostName);
                System.out.println("Port number: " + portNumber);
                return true;
            } else {
                // If result is not valid show error message
                Alert alert = new CouldNotConnectAlert();
                alert.setContentText("Invalid ip address or port number");
                alert.showAndWait();
                return false;

            }
        } else {
            System.exit(0);
        }
        return false;
    }

    public void askLoginName() {
        try {
            // Wait for commandSenderThread to connect to server
            if (latch.await(2, TimeUnit.SECONDS)) {
                // Create a new Dialog asking for login name
                LoginDialog dialog = new LoginDialog();
                Optional<String> loginResult = dialog.showAndWait();
                loginResult.ifPresent(command -> this.handleCommand(new LoginCommand(loginResult.get())));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//        if (sender.isConnected && showFrame) {
//            gui = new GUI(this);
//            Scene scene = new Scene(gui, 800, 600);
//            stage.setTitle("Two player game");
//            stage.setScene(scene);
//            stage.setX(0);
//            stage.setY(0);
//            stage.setOnCloseRequest(e -> stop());
//            stage.show();
//        } else {
////            System.exit(0);

    @Override
    public void stop() {
        commandSenderThread.interrupt();
        for (int i = 0; i < stages.size(); i++) {
            stages.get(i).close();
        }
    }

    public void setLogin(boolean loggedIn) {
        this.loggedIn = loggedIn;
        update();
    }

    public ArrayList<Integer> getPossibleMoves() {
        return board.getPossibleMoves();
    }
  
    public void showNotification(String message) {
        showNotification(message, "", "");
    }

    public void showNotification(String message, String title, String header) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification: " + title);
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        });
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
        sender.addCommand(command);
    }

    public boolean isLoggedIn() {
        return (loggedInPlayer != null);
    }

    public void startMatch(Player playerOne, Player playerTwo, GameType gameType) {
        System.out.println("your in a new match..");
        Platform.runLater(() -> {
            StartMatchAlert startMatchAlert = new StartMatchAlert();
            startMatchAlert.showAndWait();
        });
        board = new Board(gameType);
        gui.reset();
        match = new Match(gameType, playerOne, playerTwo);
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
