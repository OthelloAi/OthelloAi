package app.actors;

import app.*;
import app.exceptions.NotARealMoveException;
import app.game.Game;
import app.game.Player;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Joël Hoekstra
 */
public class MiniMaxActor implements Actor {

    private static final int DEPTH = 8;
    // weights
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
        this.board = game.getBoardObj();
        this.player = game.getLoggedInPlayer();
        board.print();
        Board b = new Board(game.getBoardObj());
        System.out.println("cloned board gametype: " + b.getGameType());
        System.out.println(player.getToken() + " -> " + player.getUsername() + " -> " + player.getOpponent().getUsername() + " -> " + player.getOpponent().getToken());
        int move;
        try {
            move = getMove(player, b);
            nextPossibleMove = move;
        } catch (NotARealMoveException e) {
            e.printStackTrace();
        }
        return nextPossibleMove;
    }

    public int negamax(Player player, Board b, int depth) {
        if (depth == 0) {
            return weightedScore(player, b.getBoard());
        }

        ArrayList<Integer> moves = b.getPossibleMoves(player);

        if (moves.size() == 0) {
            if (b.getPossibleMoves(player.getOpponent()).size() > 0) {
                return finalValue(player, b.getBoard());
            }
            return -negamax(player, b,depth - 1);
        }

        int[] values = new int[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            values[i] = valueBoard(player, b.addMove(moves.get(i), player.getToken()), depth);
        }

        int max = getMax(values);

        return moves.get(max);
    }

    // helper method for negamax
    private int valueBoard(Player player, Board b, int depth) {
        return -negamax(player.getOpponent(), b, depth - 1);
    }

    private int getMaxValue() {
        // sum(
        //  map(abs, SQUARE_WEIGHTS)
        // )
//        ArrayList<Integer> absWeights = new ArrayList<>();
//        for (int i = 0; i < weights.length; i++) {
//            absWeights.add(Math.abs(weights[i]));
//        }
        int[] absWeights = new int[weights.length];
        for (int i = 0; i < weights.length; i++) {
            absWeights[i] = Math.abs(weights[i]);
        }

        int sum = IntStream.of(weights).sum(); // http://stackoverflow.com/a/17846520
        System.out.println("getMaxValue: " + sum);
        return sum;
    }

    private int getMinValue() {
        int minValue = -getMaxValue();
        System.out.println("getMinValue: " + minValue);
        return minValue;
    }

    private int maxScore(Player player, Board b) {
        ArrayList<Integer> moves = b.getPossibleMoves(player);
        int[] scores = new int[moves.size()];
        System.out.println("SCORES: ");
        for (int i = 0; i < moves.size(); i++) {
            scores[i] = scoreMove(player, b.addMove(i, player.getToken()));
            System.out.println("move " + moves.get(i) + " - i: " + i + " score: " + scores[i]);
        }

        int max = getMax(scores);

//        OptionalInt max = IntStream.of(scores).max();
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

    private int finalValue(Player player, Token[][] b) {
        int diff = score(player, b);
        if (diff < 0) {
            return getMinValue();
        } else if (diff > 0) {
            return getMaxValue();
        }
        return diff;
    }

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


    private int minimax(Token[][] b) {
        return -1;
    }
}