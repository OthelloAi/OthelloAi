package app;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Joël Hoekstra
 */
public class Board {

    private Token[][] board = null;
    private GameType gameType;
    private Random rand;

    public Board(GameType gameType) {
        this.gameType = gameType;
        rand = new Random();
        generateBoard();
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
                    board[y][x] = new Token(' ');
                }
            }
        }

        if (gameType == GameType.REVERSI) {
            board = new Token[8][8];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
//                    board[y][x] = new Token(alphabet[y*8+x]);
//                    if ((y * 8 + x) == 27) board[y][x] = new Token('O');
//                    if ((y * 8 + x) == 28) board[y][x] = new Token('X');
//                    if ((y * 8 + x) == 35) board[y][x] = new Token('X');
//                    if ((y * 8 + x) == 36) board[y][x] = new Token('O');

                }
            }
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
            }
        }

        return moves;
    }

    public boolean isValidMove(Move move, Token token) {
        return true;
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
