package app.responses;

import java.util.ArrayList;

/**
 * @author js
 */
public class GameListResponse implements Response {

    private ArrayList<String> gameList;

    public GameListResponse(ArrayList<String> gameList) {
        this.gameList = gameList;
    }

    @Override
    public void handle() {
        System.out.println("processing GameListResponse");
        for (String game : gameList) {
            System.out.println(game);
        }
    }
}
