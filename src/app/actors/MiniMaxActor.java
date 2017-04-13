package app.actors;

import app.*;
import app.game.Game;
import app.game.Player;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class MiniMaxActor implements Actor {

    private Player player;
    private Game game;
    private Board board;

    public MiniMaxActor(Game game, Board board) {
        this.game = game;
        this.board = board;
        player = game.getLoggedInPlayer();
    }

    @Override
    public int getNext(ArrayList<Integer> possibleMoves) {
        return possibleMoves.get(0);
    }
}
