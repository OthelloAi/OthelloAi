package app.network;

import app.*;
import app.game.Game;
import app.game.GameType;
import app.game.Player;
import app.network.commands.Command;
import app.network.commands.LoginCommand;
import app.network.commands.SubscribeCommand;
import app.network.responses.*;
import app.utils.Config;
import app.utils.Debug;

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
    private boolean running;
    private App app;

    public InputHandler(App app, BufferedReader reader, CommandSender sender) {
        this.reader = reader;
        this.sender = sender;
        this.app = app;
        running = true;
    }

    private Response parseResponse(String responseString) {
        if (responseString.startsWith("SVR")) {
            if (responseString.startsWith("GAMELIST", 4)) {
                ArrayList<String> params = parseParameters(responseString.substring(14));
                return new GameListResponse(params);
            }

            if (responseString.startsWith("PLAYERLIST", 4)) {
                ArrayList<String> params = parseParameters(responseString.substring(14));
                return new PlayerListResponse(app, params);
            }

            if (responseString.startsWith("GAME MATCH", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(16, responseString.length() - 1));
                return new GameMatchResponse(app, params);
            }

            if (responseString.startsWith("GAME YOURTURN", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(19, responseString.length() - 1));
                return new YourTurnResponse(app);
            }

            if (responseString.startsWith("GAME CHALLENGE", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(20, responseString.length() - 1));
                return new ChallengeResponse(app, params);
            }

            if (responseString.startsWith("GAME MOVE", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(15, responseString.length() - 1));
                return new ReceiveMoveResponse(app, params);
            }

            if (responseString.startsWith("GAME WIN", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(14, responseString.length() - 1));
                return new GameWinResponse(app, params);
            }

            if (responseString.startsWith("GAME DRAW", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(15, responseString.length() - 1));
                return new GameDrawResponse(app, params);
            }

            if (responseString.startsWith("GAME LOSS", 4)) {
                Map<String, String> params = parseAssocParams(responseString.substring(15, responseString.length() - 1));
                return new GameLossResponse(app, params);
            }
        } else if (responseString.equals("OK")) {
            return handleResponseToCommand(sender.getOldestSentCommand());
        } else if (responseString.startsWith("ERR")) {
            if (!responseString.startsWith("Cannot challenge self", 4)) {
                sender.getOldestSentCommand();
                return handleErrorResponse(responseString);
            }
        }
        return new NullResponse();
    }

    private Response handleResponseToCommand(Command command) {
        Response response = null;

        if (command instanceof SubscribeCommand) {
            String gameName = command.toString().split(" ")[1];
            GameType gameType = Config.getGameTypeFromName(gameName);
            response = new SubscribeResponse(app, new Subscribe(gameType));
        }

        if (command instanceof LoginCommand) {
            String username = command.toString().split(" ")[1];
            response = new LoginSuccessResponse(app, new Player(username));
        }

        return response;
    }

    private Response handleErrorResponse(String responseString) {
        return new ErrorResponse(app, responseString);
    }

    @Override
    public void run() {
        Response response;
        while (running) {
            String responseString;
            try {
                if ((responseString = reader.readLine()) != null) {
                    response = parseResponse(responseString);
//                    Debug.println(response);
                    if (response != null) {
                        response.handle();
                    }
                }
            } catch (SocketException e) {
                Debug.println("Socket was closed " + e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> parseParameters(String params) {
        ArrayList<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(params);
        while (m.find()) {
            String group = m.group(1);
//            Debug.println("ParseParams -> group: " + group);
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
//            Debug.println("keyValue[1]: " + keyValue[1]);
            String value = keyValue[1].substring(1, keyValue[1].length() - 1);
            map.put(key, value);
        }
        return map;
    }
}
