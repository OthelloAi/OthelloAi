package app;

import app.game.GameType;
import app.game.Move;
import app.game.Player;
import app.utils.Debug;

import java.util.*;

/**
 * @author Joël Hoekstra
 * @author Martijn Snijder
 */
public class Board {
    private Token[][] board = null;
    private GameType gameType;

    public Board(GameType gameType) {
        this.gameType = gameType;
        generateBoard();
    }

    public Board(Board b) {
        this(b.getGameType(), b.getBoard());
    }

    public Board(GameType gameType, Token[][] board) {
        this.gameType = gameType;
        this.board = deepCopy(board);
//        print();
    }

    public void print() {
        return;
//        if (board == null) {
//            Debug.println("BOARD IS NULL");
//            return;
//        }
//        Debug.println();
//        for (int y = 0; y < board.length; y++) {
//            for (int x = 0; x < board.length; x++) {
//                Debug.print(" " + board[y][x]);
//            }
//            Debug.println();
//        }
//        Debug.println();
    }
    public GameType getGameType() {
        return gameType;
    }

    private void generateBoard() {
        if (gameType == GameType.TIC_TAC_TOE) {
            board = new Token[3][3];
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    board[y][x] = new Token(TokenState.EMPTY);
                }
            }
        }

        if (gameType == GameType.REVERSI) {
            board = new Token[8][8];
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    board[y][x] = new Token(TokenState.EMPTY);
                }
            }
            board[3][3] = new Token(TokenState.WHITE);
            board[3][4] = new Token(TokenState.BLACK);
            board[4][3] = new Token(TokenState.BLACK);
            board[4][4] = new Token(TokenState.WHITE);
        }
    }


    public boolean isValidMove(int position, Token token) {
        if (gameType == GameType.TIC_TAC_TOE) {
            return true;
        }

        int posY = position / board.length;
        int posX = position % board.length;
        Token currentMove = board[posY][posX];

        TokenState playerToken = token.getState();
        TokenState enemyToken = token.getState().opposite();

        // Check whether the chosen position is empty or not
        TokenState currentState = currentMove.getState();
        if (currentState == TokenState.EMPTY || currentState == TokenState.POSSIBLE || currentState == TokenState.BEST) {
            // Loop through all directions
            for (int x = -1; x <=1; x++) {
                for (int y = -1; y <= 1; y++) {
                    int offset = 1; // Used to look further into the same direction

                    while (inBounds(posX + (x * offset), posY + (y * offset)) && board[posY + (y * offset)][posX + (x * offset)].getState() == enemyToken) { // While there are tokens in opposite color in a direction
                        offset++; // Add 1 to offset
                    }
                    if (offset == 1) // If the offset is still equal to one, meaning that there were no opposite tokens found, continue in the loop
                        continue;
                    if (inBounds(posX + (x * offset), posY + (y * offset))) {
                        if (board[posY + (y * offset)][posX + (x * offset)].getState() == playerToken) // Check whether a token of some color was found after the opposite one
                            return true; // If so, the move is valid
                    }
                }
            }
        }
        // If boolean cant return true, it'll end up here and return false
        return false;

    }

    public boolean isValidMove(Move move, Token token) {
        return isValidMove(move.getPosition(), token);
    }

    public boolean inBounds(int x, int y) {
        return ((x >= 0) && (y >= 0) && (x < board.length) && (y < board.length));
    }

    public void flipColors(int position, Token token) {
        int posY = position / board.length;
        int posX = position % board.length;

        TokenState playerToken = token.getState();
        TokenState enemyToken = token.getState().opposite();

            for (int x = -1; x <= 1; x++) { // Loop through every horizontal direction
                for (int y = -1; y <= 1; y++) { // Loop through every vertical direction

                    int offset = 1; // We use this to check further in the same direction

                    while (inBounds(posX + (x * offset), posY + (y * offset)) && board[posY + (y * offset)][posX + (x * offset)].getState() == enemyToken) // While we find enemies in a certain direction
                    offset++; // offset = offset + 1

                    if (inBounds(posX + (x * offset), posY + (y * offset))) {
                        if (board[posY + (y * offset)][posX + (x * offset)].getState() == playerToken) // If we do find a player after the enemies the move is allowed
                            for (int counter = 1; counter <= offset; counter++) // Select all the enemies in between the two player stones
                                board[posY + (counter * y)][posX + (counter * x)] = token; // And set them to the currently playing player's color
                    }
                }
            }
    }

    public void clear() {
        board = null;
        generateBoard();
    }

    public Token[][] getBoard() {
        return board;
    }

    public Board addMove(int position, Token token) {
        int y = position / board.length;
        int x = position % board.length;

        board[y][x] = token;

        if (gameType == GameType.REVERSI) {
            flipColors(position, token);
        }

        return this;
    }

    public ArrayList<Integer> getPossibleMoves(Player player) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                int position = y * board.length + x;
                Move move = new Move(position, player);
//                Debug.println("player -> " + player.getUsername() + " with token -> " + player.getToken());
                if (isValidMove(move, player.getToken())) {
                    possibleMoves.add(move.getPosition());
                }
            }
        }
        return possibleMoves;
    }

    public Token[][] deepCopy(Token[][] input) {
        if (input == null) {
            return null;
        }
        Token[][] result = new Token[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
}
