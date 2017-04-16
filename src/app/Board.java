package app;

import app.game.GameType;
import app.game.Move;
import app.game.MoveExplorer;
import app.game.Player;
import app.network.responses.YourTurnResponse;

import java.awt.*;
import java.util.*;

import static app.TokenState.POSSIBLE;

/**
 * @author Joël Hoekstra
 */
public final class Board {
    private Token[][] board = null;
    private GameType gameType;

    public Board(GameType gameType) {
        this.gameType = gameType;
        generateBoard();
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

    public boolean isValidMove(Move move, Token token) {
        if (gameType == GameType.TIC_TAC_TOE) {
            return true;
        }

        int position = move.getPosition();

        int posY = position / board.length;
        int posX = position % board.length;
        Token currentMove = board[posY][posX];

        TokenState playerToken = token.getState();
        TokenState enemyToken = token.getState().opposite();

        // Check whether the chosen position is empty or not
        if (currentMove.getState() == TokenState.EMPTY || currentMove.getState() == TokenState.POSSIBLE) {

            for (int x = -1; x <= 1; x++) //loop through all directions
            {
                if (posX + x < 0) //If the direction is outside of the left bounds, add 1 to x
                    continue;
                if (posX + x > board.length - 1) //If the direction is outside of the right bounds, add 1 to x
                    continue;

                // Loop through all directions
                for (int y = -1; y <= 1; y++)
                {
                    if (posY + y < 0) //If the direction is outside of the upper bounds, add 1 to y
                        continue;
                    if (posY + y > board.length - 1) //if the direction is outside of the lower bounds, add 1 to y
                        continue;
                    try
                    {
                        int offset = 1; // Used to look further into the same direction
                        while (board[posY + (y * offset)][posX + (x * offset)].getState() == enemyToken) // While there are tokens in opposite color in a direction
                            offset++; // Add 1 to offset

                        if (offset == 1) // If the offset is still equal to one, meaning that there were no opposite tokens found, continue in the loop
                            continue;
                        if (board[posY + (y * offset)][posX + (x * offset)].getState() == playerToken) // Check whether a token of some color was found after the opposite one
                            return true; // If so, the move is valid

                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
        // If boolean cant return true, it'll end up here and return false
        return false;
    }

    public void flipColors(int position, Token token) {
        int posY = position / board.length;
        int posX = position % board.length;

        TokenState playerToken = token.getState();
        TokenState enemyToken = token.getState().opposite();

            for (int x = -1; x <= 1; x++) { // Loop through every horizontal direction
                for (int y = -1; y <= 1; y++) { // Loop through every vertical direction
                    try {
                        int offset = 1; // We use this to check further in the same direction

                        while (board[posY + (y * offset)][posX + (x * offset)].getState() == enemyToken) // While we find enemies in a certain direction
                            offset++; // offset = offset + 1

                        if (board[posY + (y * offset)][posX + (x * offset)].getState() == playerToken) // If we do find a player after the enemies the move is allowed
                            for (int counter = 1; counter <= offset; counter++) // Select all the enemies in between the two player stones
                                board[posY + (counter * y)][posX + (counter * x)] = token; // And set them to the currently playing player's color
                    }
                    catch (Exception e) {
                        System.out.println(e);
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

    public void addMove(int position, Token token) {
        int y = position / board.length;
        int x = position % board.length;

        board[y][x] = token;

        if (gameType == GameType.REVERSI) {
            flipColors(position, token);
        }
    }
}
