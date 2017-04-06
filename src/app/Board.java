package app;

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
                    board[y][x] = new Token('.');
                }
            }
        }

        if (gameType == GameType.REVERSI) {
            board = new Token[8][8];
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    if (rand.nextInt(20) >= 10) {
                        token = new Token('X');
                    } else {
                        token = new Token('O');
                    }
                    board[y][x] = token;
                }
            }
        }
    }

    public Token[][] getBoard() {
        return board;
    }

    public void addMove(int position, Token token) {
        int y = position / board.length;
        int x = position % board.length;

        board[y][x] = token;
    }

}
