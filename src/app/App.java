package app;

import app.game.Game;
import app.gui.GUI;
import app.gui.alerts.CouldNotConnectAlert;
import app.gui.dialogs.ConnectionDialog;
import app.network.CommandSender;
import app.network.Connection;
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
    private Game game;

    public App() {
        game = new Game();
        connection.connect();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stages.add(stage);
        while (true) {
            if (askUserToConnect()) {
                Connection.getInstance().connect();
                if (Connection.getInstance().isConnected()) {
                    sender = new CommandSender(game, Connection.getInstance().getSocket());
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

        GUI gui = new GUI(this);
        Scene scene = new Scene(gui, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Tic-Tac-Toe | Reversi client");
        stage.show();
        stage.setOnCloseRequest(e -> {
            stop();
        });
    }

    public Game getGame() {
        return game;
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
//                System.out.println("hostname: " + hostName);
//                System.out.println("Port number: " + portNumber);
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
        for (Stage stage : stages) {
            stage.close();
        }
    }
}
