package app.network.responses;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class GameListResponse implements Response {

    private ArrayList<String> gameList;

    public GameListResponse(ArrayList<String> gameList) {
        this.gameList = gameList;
    }

    @Override
    public void handle() {
    }
}
