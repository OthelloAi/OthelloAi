package app.actors;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public interface Actor {
    public int getNext(ArrayList<Integer> possibleMoves);
}