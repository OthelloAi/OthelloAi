package app.actors;

import app.*;
import app.exceptions.NotARealMoveException;
import app.game.Game;
import app.game.Move;
import app.game.Player;
import app.utils.Debug;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Joël Hoekstra
 */
public class MiniMaxActor implements Actor {

    private static final int DEPTH = 10;
    // weights
    private int[] weights2 = {
            120, -20, 20,  5,  5, 20, -20, 120,
            -20, -40, -5, -5, -5, -5, -40, -20,
             20,  -5, 15,  3,  3, 15,  -5,  20,
              5,  -5,  3,  3,  3,  3,  -5,   5,
              5,  -5,  3,  3,  3,  3,  -5,   5,
             20,  -5, 15,  3,  3,  3,  -5,  20,
            -20, -40, -5, -5, -5, -5, -40, -20,
            120, -20, 20,  5,  5, 20, -20, 120,
    };

    private int[] weights = {
            8, 2, 7, 6, 6, 7, 2, 8,
            2, 1, 3, 3, 3, 3, 1, 2,
            7, 3, 5, 4, 4, 5, 3, 7,
            6, 3, 4, 0, 0, 4, 3, 6,
            6, 3, 4, 0, 0, 4, 3, 6,
            7, 3, 5, 4, 4, 5, 3, 7,
            2, 1, 3, 3, 3, 3, 1, 2,
            8, 2, 7, 6, 6, 7, 2, 8
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
        int[] absWeights = new int[board.getBoard().length];
        for (int i = 0; i < board.getBoard().length; i++) {
            absWeights[i] = Math.abs(weights[i]);
        }

        int sum = IntStream.of(weights).sum(); // http://stackoverflow.com/a/17846520
//        Debug.println("getMaxValue: " + sum);
        return sum;
    }

    private int getMinValue() {
        int minValue = -getMaxValue();
//        Debug.println("getMinValue: " + minValue);
        return minValue;
    }

    private int maxScore(Player player, Board b) {
        ArrayList<Integer> moves = b.getPossibleMoves(player);
        int[] scores = new int[moves.size()];
//        Debug.println("SCORES: ");
        for (int i = 0; i < moves.size(); i++) {
            scores[i] = scoreMove(player, b.addMove(i, player.getToken()));
//            Debug.println("move " + moves.get(i) + " - i: " + i + " score: " + scores[i]);
        }

        int max = getMax(scores);
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

//    public Move chooseMove(Player player) {
//        ArrayList<Integer> possibleMoves = board.getPossibleMoves(player);
//        Map<Move, ArrayList<>>
//    }

    public int getHeuristic(Board b, Move move) {
        Player player = move.getPlayer();
        int heuristic = 0;

        ArrayList<Integer> possibleMoves = b.getPossibleMoves(player);

        for (Integer position : possibleMoves) {
            heuristic += getHeuristicFromPosition(position, player.getToken(), b);
        }

        return heuristic;
    }

    public int getHeuristicFromPosition(int position, Token token, Board b) {
        int heuristic = 0;
        switch(position) {
            case 0:
            case 7:
            case 56:
            case 63:
                if (b.getTokenOnPosition(position) == token) {
                    heuristic = 50;
                    break;
                }
            case 3:
            case 4:
            case 18:
            case 21:
            case 24:
            case 31:
            case 32:
            case 39:
            case 42:
            case 45:
            case 59:
            case 60:
                if (b.getTokenOnPosition(position) == token) {
                    heuristic = 10;
                }
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 17:
            case 22:
            case 25:
            case 27:
            case 28:
            case 30:
            case 33:
            case 35:
            case 36:
            case 38:
            case 41:
            case 46:
            case 50:
            case 51:
            case 52:
            case 53:
                if (b.getTokenOnPosition(position) == token) {
                    heuristic = -10;
                }
                break;
            case 19:
            case 20:
            case 26:
            case 29:
            case 34:
            case 37:
            case 43:
            case 44:
                if (b.getTokenOnPosition(position) == token) {
                    heuristic = -50;
                }
                break;
            case 1:
            case 8:
            case 9:
                if (b.getTokenOnPosition(position) == token) {
                    if (b.getTokenOnPosition(0) == token) {
                        heuristic = 75;
                    } else {
                        heuristic = -75;
                    }
                }
                break;
            case 6:
            case 14:
            case 15:
                if (b.getTokenOnPosition(position) == token) {
                    if (b.getTokenOnPosition(7) == token) {
                        heuristic = 75;
                    } else {
                        heuristic = -75;
                    }
                }
                break;
            case 48:
            case 49:
            case 57:
                if (b.getTokenOnPosition(position) == token)
                    if (b.getTokenOnPosition(56) == token)
                        heuristic = 75;
                    else
                        heuristic = -75;
                break;
            case 54:
            case 55:
            case 62:
                if (b.getTokenOnPosition(position) == token)
                    if (b.getTokenOnPosition(63) == token)
                        heuristic = 75;
                    else
                        heuristic = -75;
                break;
        }
        return heuristic;
    }
}