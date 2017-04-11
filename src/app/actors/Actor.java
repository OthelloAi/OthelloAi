package app.actors;

import app.Move;
import app.Token;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public interface Actor {
    public Move getNextMove(ArrayList<Move> possibleMoves);
    public int getNext(ArrayList<Integer> possibleMoves);
}