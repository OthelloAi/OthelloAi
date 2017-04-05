package app.responses;

import java.util.ArrayList;

/**
 * @author js
 */
public class PlayerListResponse implements Response {
    private ArrayList<String> playerList;

    public PlayerListResponse(ArrayList<String> playerList) {
        this.playerList = playerList;
    }

    @Override
    public void handle() {
        System.out.println("processing PlayerListResponse");
        for (String player : playerList) {
            System.out.println(player);
        }
    }
}
