package app;

import app.commands.LoginCommand;
import app.gui.alerts.CouldNotConnectAlert;
import app.gui.dialogs.ConnectionDialog;
import app.gui.dialogs.LoginDialog;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gabe on 12-04-17.
 */
public class Connection implements Protocol{
    private boolean isConnected = false;
    private Socket socket = null;
    private String hostName = SERVER_HOST;
    private int portNumber = SERVER_PORT;
    private CountDownLatch latch = new CountDownLatch(1);

    public Connection() {
    }

    public Socket getSocket() {
        return socket;
    }

    public void connect(String hostName, int portNumber) {
        try {
            socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            System.out.println("Error while attempting to establish a connection");
            } catch (Exception e) {
            e.printStackTrace();
        }

        if (socket != null) {
            this.isConnected = true;
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

}
