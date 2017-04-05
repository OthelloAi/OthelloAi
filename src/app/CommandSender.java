package app;

import app.commands.Command;
import app.commands.NullCommand;
import app.responses.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

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

    private Game game;

    public CommandSender(Game game) {
        this.game = game;
        socket = null;
        commands = new ArrayList<>();
        sentCommands = new LinkedList<>();
        responses = new ArrayList<>();
        running = true;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            inputHandler = new InputHandler(game, in, this);
            Thread thread = new Thread(inputHandler);
            thread.setDaemon(true);
            thread.start();

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
