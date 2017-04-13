package app.actors;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class IterativeActor implements Actor {
    @Override
    public int getNext(ArrayList<Integer> possibleMoves) {
        return possibleMoves.get(0);
    }
}
