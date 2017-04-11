package app.actors;

import app.Move;

import java.util.ArrayList;

import java.util.Random;
/**
 * @author Joël Hoekstra
 */
public class RandomActor implements Actor {

    private Random rand = new Random();

    @Override
    public Move getNextMove(ArrayList<Move> possibleMoves) {
        return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }

    @Override
    public int getNext(ArrayList<Integer> possibleMoves) {
        return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }
}
