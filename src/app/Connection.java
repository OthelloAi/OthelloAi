package app;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Gabe on 12-04-17.
 */
public class Connection implements Protocol{
    private boolean isConnected = false;
    private Socket socket = null;

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
