package app.network.responses;

import app.game.Game;
import app.game.Player;

import java.util.ArrayList;

/**
 * @author js
 */
public class PlayerListResponse implements Response {
    private ArrayList<String> playerList;
    private Game game;

    public PlayerListResponse(Game game, ArrayList<String> playerList) {
        this.game = game;
        this.playerList = playerList;
    }

    @Override
    public void handle() {
        ArrayList<Player> players = new ArrayList<>();
        for (String player : playerList) {
            players.add(new Player(player));
        }

        game.setPlayers(players);
    }
}
