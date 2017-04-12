package app;

import app.commands.Command;
import app.commands.NullCommand;
import app.gui.alerts.CouldNotConnectAlert;
import app.gui.dialogs.ConnectionDialog;
import app.responses.Response;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * @author Joël Hoekstra
 */
public class CommandSender implements Protocol, Runnable {
    private static ArrayList<Command> commands;
    private LinkedList<Command> sentCommands;
    private static ArrayList<Response> responses;
    private Socket socket;
    private boolean running;
    private InputHandler inputHandler;
    private int connectionAttempts = 0;
    private CountDownLatch latch;
    private CountDownLatch latch2;
    private Game game;
    private String hostName= SERVER_HOST;
    private int portNumber = SERVER_PORT;

    public CommandSender(Game game, CountDownLatch latch, Socket socket) {
        this.latch = latch;
        this.game = game;
        this.socket = socket;
        commands = new ArrayList<>();
        sentCommands = new LinkedList<>();
        responses = new ArrayList<>();
        running = true;
    }

    @Override
    public void run() {
        try {
            if (socket != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                inputHandler = new InputHandler(game, in, this);
                Thread thread = new Thread(inputHandler);
                thread.setDaemon(true);
                thread.start();
                latch.countDown();

                while (running) {
                    if (commands.size() > 0) {
                        Command command = commands.get(0);
                        out.println(command.toString());
                        addSentCommand(command);
                        System.out.println("[CLNT] command: " + command.toString());
                        commands.remove(0);
                    }

                    if (responses.size() > 0) {
                        Response response = responses.get(0);
                        response.handle();
                        System.out.println("[CLNT] response handled");
                    }
                    Thread.sleep(10);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addResponse(Response response) {
        responses.add(response);
    }

    public static void addCommand(Command command) {
        commands.add(command);
    }


    public void addSentCommand(Command command) {
        sentCommands.addLast(command);
    }

    public Command getOldestSentCommand() {
        if (sentCommands.size() > 0) {
            return sentCommands.removeFirst();
        } else {
            return new NullCommand();
        }
    }

//    public void reconnect() {
//        if (connectionAttempts > MAX_CONNECTION_ATTEMPTS) {
//            System.out.println("Could not establish a connection.");
//            Platform.runLater(() -> {
//                game.stop();
//            });
//        } else {
//            System.out.println("Attempting to establish a connection in 1 seconds...");
//            System.out.println("Attempt " + connectionAttempts + "/" + MAX_CONNECTION_ATTEMPTS);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {}
//            connect();
//        }
//    }
}