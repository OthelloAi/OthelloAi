package app;

import app.commands.Command;
import app.commands.LoginCommand;
import app.commands.SubscribeCommand;
import app.responses.*;

import java.io.BufferedReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joël Hoekstra
 */
public class InputHandler implements Runnable {

    private BufferedReader reader;
    private CommandSender sender;
    private Map<String, String> params;
    private boolean running;
    private Game game;

    public InputHandler(Game game, BufferedReader reader, CommandSender sender) {
        this.reader = reader;
        this.sender = sender;
        this.game = game;
        this.params = new HashMap<>();
        running = true;
    }

    private Response parseResponse(String responseString) {
        System.out.println("IN: " + responseString);
        if (responseString.startsWith("SVR")) {
            System.out.println("SERVER MESSAGE: " + responseString);
            if (responseString.startsWith("GAMELIST", 4)) {
                ArrayList<String> params = parseParameters(responseString.substring(14));
                return new GameListResponse(params);
            }

            if (responseString.startsWith("PLAYERLIST", 4)) {
                ArrayList<String> params = parseParameters(responseString.substring(14));
                return new PlayerListResponse(game, params);
            }

            if (responseString.startsWith("GAME MATCH", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(16));
                System.out.println("game match, params:");
                for (String key : params.keySet()) {
                    System.out.println(key + ": " + params.get(key));
                }
            }

            if (responseString.startsWith("GAME YOURTURN", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(19, responseString.length() - 1));
                // do your move here
                // @todo remove this later because @Martijn has made this.
                System.out.println("game YOURTURN, params:");
                for (String key : params.keySet()) {
                    System.out.println(key + ": " + params.get(key));
                }
            }

            if (responseString.startsWith("GAME CHALLENGE", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(20, responseString.length() - 1));
                System.out.println("game challenge, params:");
                for (String key : params.keySet()) {
                    System.out.println(key + ": " + params.get(key));
                }
                return new ChallengeResponse(params);
            }

            if (responseString.startsWith("GAME MOVE", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(15, responseString.length() - 1));
                System.out.println("game MOVE, params:");
                for (String key : params.keySet()) {
                    System.out.println(key + ": " + params.get(key));
                }
            }

            if (responseString.startsWith("GAME WIN", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(14, responseString.length() - 1));
                System.out.println("game WIN, params:");
                for (String key : params.keySet()) {
                    System.out.println(key + ": " + params.get(key));
                }
            }

            if (responseString.startsWith("GAME LOSS", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(15, responseString.length() - 1));
                System.out.println("game LOSS, params:");
                for (String key : params.keySet()) {
                    System.out.println(key + ": " + params.get(key));
                }
            }
        } else if (responseString.equals("OK")) {
            Command responseTo = sender.getOldestSentCommand();
            System.out.println("[SVR] Accepted the command '" + responseTo.toString() + "'");

            return handleResponseToCommand(responseTo);
        } else if (responseString.startsWith("ERR")) {
            Command errorResponseTo = sender.getOldestSentCommand();
            System.err.println("[SVR] Error occurred to command: '" + errorResponseTo.toString() + "'");
            return handleErrorResponse(errorResponseTo);
        }
        return new NullResponse();
    }

    private Response handleResponseToCommand(Command command) {
        Response response = null;

        if (command instanceof SubscribeCommand) {
            String gameName = command.toString().split(" ")[1];
            GameType gameType = Config.getGameTypeFromName(gameName);
            response = new SubscribeResponse(game, new Subscribe(gameType));
        }

        if (command instanceof LoginCommand) {
            response = new LoginSuccessResponse(game);
        }

        return response;
    }

    private Response handleErrorResponse(Command command) {
        Response response = null;
        if (command instanceof LoginCommand) {
            response = new AlreadyLoggedInResponse(game);
        }
        return response;
    }

    @Override
    public void run() {
        Response response;
        while (running) {
            String responseString;
            try {
                if ((responseString = reader.readLine()) != null) {
                    response = parseResponse(responseString);
                    System.out.println(response);
                    if (response != null) {
                        response.handle();
                    }
                }
            } catch (SocketException e) {
                System.err.println("Socket was closed " + e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void terminate() {
        running = false;
    }

    public ArrayList<String> parseParameters(String params) {
        ArrayList<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(params);
        while (m.find()) {
            String group = m.group(1);
            System.out.println("ParseParams -> group: " + group);
            list.add(group);
        }
        return list;
    }

    public Map<String, String> parseAssocParams(String params) {
        Map<String, String> map = new HashMap<>();
        String[] explodedParams = params.split(", ");
        for (int i = 0; i < explodedParams.length; i++) {
            String[] keyValue = explodedParams[i].split(": ");
            String key = keyValue[0];
            System.out.println("keyValue[1]: " + keyValue[1]);
            String value = keyValue[1].substring(1, keyValue[1].length() - 1);
            map.put(key, value);
        }
        return map;
    }
}
