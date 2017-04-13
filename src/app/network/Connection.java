package app.network;

import app.Protocol;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Gabe on 12-04-17.
 */
public class Connection implements Protocol {
    private boolean isConnected = false;
    private Socket socket = null;
    private String host = "localhost";
    private int port = 7789;

    private static Connection instance = new Connection();

    private Connection() { }

    public static Connection getInstance() {
        return instance;
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

    public void connect() {
        connect(host, port);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public static void setHost(String host) {
        Connection.getInstance().host = host;
    }

    public static void setPort(int port) {
        Connection.getInstance().port = port;
    }
}
