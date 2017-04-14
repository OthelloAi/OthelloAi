package app.network.responses;

import app.App;
import app.game.Game;
import app.game.Player;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class PlayerListResponse implements Response {
    private ArrayList<String> playerList;
    private App app;

    public PlayerListResponse(App app, ArrayList<String> playerList) {
        this.app = app;
        this.playerList = playerList;
    }

    @Override
    public void handle() {
        ArrayList<Player> players = new ArrayList<>();
        for (String player : playerList) {
            players.add(new Player(player));
        }
        app.setOnlinePlayers(players);
    }
}
