package app;

import app.game.Game;
import app.game.Player;
import app.gui.GUI;
import app.gui.alerts.CouldNotConnectAlert;
import app.gui.dialogs.ConnectionDialog;
import app.network.CommandSender;
import app.network.Connection;
import app.network.commands.LogoutCommand;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joël Hoekstra
 */
public class App extends Application {

    private ArrayList<Stage> stages = new ArrayList<>();
    private Connection connection = Connection.getInstance();
    private CommandSender sender;
    private Thread commandSenderThread;
    private GUI gui;
    private Player user;
    private Game game;

    private ArrayList<Player> onlinePlayers = new ArrayList<>();

    public App() {
        game = new Game(this);
        connection.connect();
    }

    public void addUser(Player user) {
        this.user = user;
    }

    public Player getUser() {
        return user;
    }

    public void setOnlinePlayers(ArrayList<Player> players) {
        this.onlinePlayers = players;
    }

    public ArrayList<Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stages.add(stage);
        while (true) {
            if (askUserToConnect()) {
                Connection.getInstance().connect();
                if (Connection.getInstance().isConnected()) {
                    sender = new CommandSender(this, Connection.getInstance().getSocket());
                    commandSenderThread = new Thread(sender);
                    commandSenderThread.setDaemon(true);
                    commandSenderThread.start();
                    break;
                } else {
                    Alert alert = new CouldNotConnectAlert();
                    alert.setContentText("Could not connect to server");
                    alert.showAndWait();
                }
            }
        }

        gui = new GUI(this);
        // TODO: 15-4-2017 Dynamically changing scene size
        Scene scene = new Scene(gui, 630, 670);
        stage.setScene(scene);
        stage.setTitle("Tic-Tac-Toe | Reversi client");
        stage.show();
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            stop();
        });
    }

    public void addStage(Stage stage) {
        stages.add(stage);
    }

    public Game newGame() {
        // TODO: 14/04/2017 create a new game object and update gui, etc with this.
        return game;
    }
    public Game getGame() {
        return game;
    }
    public GUI getGUI() {
        return gui;
    }

    private boolean askUserToConnect() {
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
                String hostName = str.substring(0, str.indexOf(":"));
                int portNumber = Integer.parseInt(str.substring(str.indexOf(":")+1));
                Connection.getInstance().setPort(portNumber);
                Connection.getInstance().setHost(hostName);
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

    @Override
    public void stop() {
        CommandSender.addCommand(new LogoutCommand());
        for (Stage stage : stages) {
            if (stage != null) {
                stage.close();
            }
        }
    }
}
