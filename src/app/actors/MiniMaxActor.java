package app.actors;

import app.*;
import app.exceptions.NotARealMoveException;
import app.game.Game;
import app.game.GameType;
import app.game.Player;

import java.util.*;
import java.util.stream.IntStream;

/**
 * ONLY WORKS FOR REVERSI!!
 * @author Joël Hoekstra
 */
public class MiniMaxActor implements Actor {

    private static final int DEPTH = 3;

    private int[] weights = {
            120, -20, 20,  5,  5, 20, -20, 120,
            -20, -40, -5, -5, -5, -5, -40, -20,
            20,  -5, 15,  3,  3, 15,  -5,  20,
            5,  -5,  3,  3,  3,  3,  -5,   5,
            5,  -5,  3,  3,  3,  3,  -5,   5,
            20,  -5, 15,  3,  3,  3,  -5,  20,
            -20, -40, -5, -5, -5, -5, -40, -20,
            120, -20, 20,  5,  5, 20, -20, 120,
    };

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
        int nextPossibleMove = possibleMoves.get(0);

        if (game.getGameType() == GameType.TIC_TAC_TOE) {
            return nextPossibleMove;
        }

        this.board = game.getBoardObj();
        this.player = game.getLoggedInPlayer();
        Board b = new Board(game.getBoardObj());
        int move;
        try {
            move = getMove(player, b);
            nextPossibleMove = move;

            // check if the ai can grab a corner and if so DO IT!
            for (int i = 0; i < possibleMoves.size(); i++) {
                if (possibleMoves.get(i) == 0) {
                    return possibleMoves.get(i);
                }
                if (possibleMoves.get(i) == 7) {
                    return possibleMoves.get(i);
                }
                if (possibleMoves.get(i) == 56) {
                    return possibleMoves.get(i);
                }
                if (possibleMoves.get(i) == 63) {
                    return possibleMoves.get(i);
                }
            }
        } catch (NotARealMoveException e) {
            e.printStackTrace();
        }
        return nextPossibleMove;
    }

    // recursive negamax method
    public int negamax(Player player, Board b, int depth) {
        if (depth == 0) {   // if final depth is reached calculate the weighted score of the board
            return weightedScore(player, b.getBoard());
        }

        ArrayList<Integer> moves = b.getPossibleMoves(player);

        if (moves.size() == 0) {    // if there are moves left for the player
            if (b.getPossibleMoves(player.getOpponent()).size() > 0) { // check if there are possible moves for the opponent
                return finalValue(player, b.getBoard()); // when there are moves left check the final value
            }
            return -negamax(player, b,depth - 1); // reverse the negamax and go deeper
        }

        int[] values = new int[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            // get values from a level deeper and from the opponent
            values[i] = -negamax(player.getOpponent(), b.addMove(moves.get(i), player.getToken()), depth - 1);
        }
        // get max valued move number
        int max = getMax(values);

        // return max move
        return moves.get(max);
    }

    // gets the maximum value by sum of the weights
    private int getMaxValue() {
        int[] absWeights = new int[board.getBoard().length];
        for (int i = 0; i < board.getBoard().length; i++) {
            absWeights[i] = Math.abs(weights[i]);
        }

        int sum = IntStream.of(weights).sum(); // http://stackoverflow.com/a/17846520
        return sum;
    }

    // gets the max scoring move from the possible moves
    private int maxScore(Player player, Board b) {
        ArrayList<Integer> moves = b.getPossibleMoves(player);
        int[] scores = new int[moves.size()]; // list with scores
        for (int i = 0; i < moves.size(); i++) {
            // add the score from the given move to the list
            scores[i] = scoreMove(player, b.addMove(i, player.getToken()));
        }
        // get max score from all moves
        int max = getMax(scores);
        // get the value associated to the max value
        return moves.get(max);
    }

    public int getMax(int[] arr) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[max] > arr[i]) {
                max = i;
            }
        }
        return max;
    }

    private int getMove(Player player, Board b) throws NotARealMoveException {
        Board copy = new Board(b);
        int move = maxScore(player, copy);
        if (!b.isValidMove(move, player.getToken())) {
            throw new NotARealMoveException(move + " is not a real move.");
        }
        return move;
    }

    // helper method for maxScore method
    private int scoreMove(Player player, Board b) {
        return negamax(player, b, DEPTH);
    }

    // calculates the score for the player based upon the difference created in the score method.
    // if the score is below zero return minvalue and higher return maxvalue else return the score
    private int finalValue(Player player, Token[][] b) {
        int diff = score(player, b);
        if (diff < 0) {
            return -getMaxValue();
        } else if (diff > 0) {
            return getMaxValue();
        }
        return diff;
    }

    // calculate the amount of nodes on the field associated with both players and subtract the opponents
    // score from mine.
    private int score(Player player, Token[][] b) {
        int mine = 0;
        int theirs = 0;
        Player opp = player.getOpponent();
        for (int y = 0; y < b.length; y++) {
            for (int x = 0; x < b.length; x++) {
                Token token = b[y][x];
                if (token == player.getToken()) {
                    mine += 1;
                }
                if (token == opp.getToken()) {
                    theirs += 1;
                }
            }
        }
        return mine - theirs;
    }

    public int weightedScore(Player player, Token[][] b) {
        Token opp = player.getOpponent().getToken();
        int total = 0;
        for (int y = 0; y < b.length; y++) {
            for (int x = 0; x < b.length; x++) {
                if (b[y][x] == player.getToken()) {
                    total += weights[y * b.length + x];
                } else if (b[y][x] == opp) {
                    total -= weights[y * b.length + x];
                }
            }
        }
        return total;
    }
}