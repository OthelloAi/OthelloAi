package app.actors;

import app.Move;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class IterativeActor implements Actor {

    @Override
    public Move getNextMove(ArrayList<Move> possibleMoves) {
        return possibleMoves.get(0);
    }

    @Override
    public int getNext(ArrayList<Integer> possibleMoves) {
        return possibleMoves.get(0);
    }
}
