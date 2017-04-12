package app;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.awt.*;
import java.util.*;

/**
 * @author Joël Hoekstra
 */
public final class Board {

    public static final int BOARD_LENGTH = 8;
    public static final int BOARD_WIDTH = 8;
    private Map<Point, Token> grid;

    private Token[][] board = null;
    private GameType gameType;
    private Random rand;

    public Board(GameType gameType) {
        this.gameType = gameType;
        rand = new Random();

        grid = new HashMap<>(BOARD_LENGTH * BOARD_WIDTH);

        generateBoard();
        init();
    }

    public void init() {
        Point point = new Point();
        for (point.x = 0; point.x < BOARD_LENGTH; point.x++) {
            for (point.y = 0; point.y < BOARD_WIDTH; point.y++) {
                grid.put(new Point(point), new Token(TokenState.EMPTY));
            }
        }

        grid.put(new Point(3,3), new Token(TokenState.WHITE));
        grid.put(new Point(3,4), new Token(TokenState.BLACK));
        grid.put(new Point(3,3), new Token(TokenState.BLACK));
        grid.put(new Point(4,4), new Token(TokenState.WHITE));
    }

    public Token getToken(Point point) {
        return grid.get(point);
    }

    public Set<Point> getTokens(Token token) {
        Set<Point> points = new HashSet<>();
        for (Point point : grid.keySet()) {
            if (grid.get(point) == token) {
                points.add(point);
            }
        }
        return points;
    }

    public boolean isFull() {
        for (Point point : grid.keySet()) {
            if (grid.get(point).getState() == TokenState.EMPTY) {
                return false;
            }
        }
        return true;
    }


    public int count(Token token) {
        int count = 0;
        for (Point point : grid.keySet()) {
            if (grid.get(point) == token) {
                count++;
            }
        }
        return count;
    }

    public Set<Point> getPossibleMoves(Player player) {
        return MoveExplorer.explore(this, player);
    }

    public void markPossibleMoves(Set<Point> possibleMoves) {
        for (Point point : possibleMoves) {
            grid.put(point, new Token(TokenState.POSSIBLE));
        }
    }

    public void unmarkPossibleMoves() {
        for (Point point : grid.keySet()) {
            if (grid.get(point).getState() == TokenState.POSSIBLE) {
                grid.put(point, new Token(TokenState.EMPTY));
            }
        }
    }

    public void mark(Set<Point> points, Token token) {
        for (Point point : points) {
            grid.put(point, token);
        }
    }

    public Set<Point> makeMove(Point move, Token token) {
        grid.put(move, token);
        Set<Point> changedTokens = MoveExplorer.pointsToFill(this, move);
        mark(changedTokens, token);
        changedTokens.add(move);
        return changedTokens;
    }

    private void generateBoard() {
        Token token;
        if (gameType == GameType.TIC_TAC_TOE) {
            board = new Token[3][3];
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
//                    if (rand.nextInt(20) >= 10) {
//                        token = new Token('X');
//                    } else {
//                        token = new Token('O');
//                    }
//                    board[y][x] = token;
                    board[y][x] = new Token(TokenState.EMPTY);
                }
            }
        }

        if (gameType == GameType.REVERSI) {
            board = new Token[8][8];
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    board[y][x] = new Token(TokenState.EMPTY);
//                    if ((y * 8 + x) == 27) board[y][x] = new Token('O');
//                    if ((y * 8 + x) == 28) board[y][x] = new Token('X');
//                    if ((y * 8 + x) == 35) board[y][x] = new Token('X');
//                    if ((y * 8 + x) == 36) board[y][x] = new Token('O');
                }
            }
            board[3][3] = new Token(TokenState.WHITE);
            board[3][4] = new Token(TokenState.BLACK);
            board[4][3] = new Token(TokenState.BLACK);
            board[4][4] = new Token(TokenState.WHITE);
        }
    }

    public ArrayList<Move> allPotentialMoves(Token token, Player player) {
        ArrayList<Move> moves = new ArrayList<>();
        Move move;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                move = new Move((y * 8 + x), player);

                if (this.isValidMove(move, token)) {
                    moves.add(move);
                }
                else {
                    // // TODO: 12-4-2017 Move isn't valid 
                }
            }
        }

        return moves;
    }

    public boolean isValidMove(Move move, Token token) {

        // Get position
        int position = move.getPosition();
        int posY = position / board.length;
        int posX = position % board.length;
        Token currentMove = board[posY][posX];

        // Check whether the chosen position is empty or not
        if (currentMove.getState() == TokenState.EMPTY){
            // Loop through all directions
            for (int x = -1; x <=1; x++)
            {
                for (int y = -1; y <= 1; y++)
                {
                    int offset = 1; //Used to look further into the same direction

                    while (board[posX + (x * offset)][posY + (y * offset)].getState() == token.getState().opposite()) // While there are tokens in opposite color in a direction
                    offset++; //Add 1 to offset

                    if (offset == 1) //If the offset is still equal to one, meaning that there were no opposite tokens found, continue in the loop
                        continue;
                    if (board[posX + (x * offset)][posY + (y * offset)].getState() == token.getState()) //Check whether a token of some color was found after the opposite one
                    return true; //If so, the move is valid
                }
            }
        }
        // If boolean cant return true, it'll end up here and return false
        return false;
    }

    public void clear() {
        board = null;
        generateBoard();
    }

    public Token[][] getBoard() {
        return board;
    }

    public void addMove(int position, Token token) {
        int y = position / board.length;
        int x = position % board.length;

        board[y][x] = token;
    }

    public ArrayList<Integer> getPossibleMoves() {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (board[y][x].toString().equals(" ")) {
                    possibleMoves.add(y * board.length + x);
                }
            }
        }

        return possibleMoves;
    }

}
