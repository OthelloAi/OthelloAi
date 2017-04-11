package app.actors;

import app.*;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class MiniMaxActor implements Actor {

//    private Map<String, Integer> possibleMoves;
    private Player player;
    private Game game;
    private Board board;

    private Token playerOne = new Token(TokenState.CROSS);
    private Token playerTwo = new Token(TokenState.NOUGHT);

    private int fc = 0;

    public MiniMaxActor(Game game, Board board) {
        this.game = game;
        this.board = board;
        player = game.getLoggedInPlayer();
//        possibleMoves = new HashMap<>();

    }

    public ArrayList<Integer> emptyIndices(Token[] board) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i].getState() == TokenState.EMPTY) {
                indices.add(i);
            }
        }
        return indices;
    }


    public ScoredMove Minimax(Token[] board, Token player, int depth) {
        fc++;
        int initialDepth = depth;
        ArrayList<Integer> availableSpots = emptyIndices(board);
        ArrayList<ScoredMove> moves = new ArrayList<>();

        // check if the game is already won, lost or at a draw
        // and return a fake move with a score.
        if (winning(board, playerOne)) {
            return new ScoredMove(null, 10);
        } else  if (winning(board, playerTwo)) {
            return new ScoredMove(null, -10);
        } else if (availableSpots.size() == 0) {
            return new ScoredMove(null, 0);
        }

        for (int i = 0; i < availableSpots.size(); i++) {
            // create an object for each and store the index of that spot
            // as a number in the object's index key
            ScoredMove move = new ScoredMove();
            move.index = board[availableSpots.get(i)];

            // set the empty spot to the current player
            board[availableSpots.get(i)] = player;

            // if collect the score resulted from calling minimax on the opponent of the current player
            if (player == playerOne) {
                ScoredMove result = Minimax(board, playerOne, depth+1);
                move.score = result.score;
            } else {
                ScoredMove result = Minimax(board, playerTwo, depth+1);
                move.score = result.score;
            }

            // reset the spot to empty
            board[availableSpots.get(i)] = move.index;
            move.position = availableSpots.get(i);
            moves.add(move);
        }

        // if it is the ai's turn loop over the moves and choose the move with the highest score
        int bestMove = -1;
        int bestScore = 0;
//        System.out.println(player.toString());
        if (player == playerOne) {
//            System.out.println("player: " + player.toString() + " : " + playerOne.toString());
            bestScore = -10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score > bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        } else {
//            System.out.println("player: " + player.toString() + " : " + playerTwo.toString());
            // loop over the moves and choose the move with the lowest score
            bestScore = 10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score < bestScore) {
                    bestScore = moves.get(i).score;
                    bestMove = i;
                }
            }
        }
//        System.out.println("bestScore: " + bestScore + " bestMove: " + bestMove + " - depth: " + initialDepth);
        // return the chosen move (object) from the array with the higher depth
        return moves.get(bestMove);
    }

    public boolean winning(Token[] board, Token token) {
        if (board[0] == token && board[1] == token && board[2] == token) return true;

        if (board[3] == token && board[4] == token && board[5] == token) return true;

        if (board[6] == token && board[7] == token && board[8] == token) return true;

        if (board[0] == token && board[3] == token && board[6] == token) return true;

        if (board[1] == token && board[4] == token && board[7] == token) return true;

        if (board[2] == token && board[5] == token && board[8] == token) return true;

        if (board[0] == token && board[4] == token && board[8] == token) return true;

        if (board[2] == token && board[4] == token && board[6] == token) return true;

        return false;
    }

    public boolean winning(Token[][] board, Token token) {
        // horizontal

        // 0(0,0), 1(0,1), 2(0,2)
        if (board[0][0] == token && board[0][1] == token && board[0][2] == token) return true;

        // 3(1,0), 4(1,1), 5(1,2)
        if (board[1][0] == token && board[1][1] == token && board[1][2] == token) return true;

        // 6(2,0), 7(2,1), 8(2,2)
        if (board[2][0] == token && board[2][1] == token && board[2][2] == token) return true;

        // vertical

        // 0(0,0), 3(1,0), 6(2,0)
        if (board[0][0] == token && board[1][0] == token && board[2][0] == token) return true;

        // 1(0,1), 4(1,1), 7(2,1)
        if (board[0][1] == token && board[1][1] == token && board[2][1] == token) return true;

        // 2(0,2), 5(1,2), 8(2,2)
        if (board[0][2] == token && board[1][2] == token && board[2][2] == token) return true;

        // diagonal left to right

        // 0(0,0), 4(1,1), 8(2,2)
        if (board[0][0] == token && board[1][1] == token && board[2][2] == token) return true;

        // diagonal right to left

        // 6(2,0), 4(1,1), 2(0,2)
        if (board[2][0] == token && board[1][1] == token && board[0][2] == token) return true;

        return false;
    }



//    private void MiniMax() {
//        // let move be an object corresponding to a move, scoredmove be an object corresponding to a move and its score.
//
//        Move move;
//        ScoredMove scoredMove;
//
//        ScoredMove bestSoFar = new ScoredMove(); // default
//        ScoredMove result;
//
//        // if the game is over, return a fake move and the score
//        if (game is draw){
//            return new ScoredMove(null, 0);
//        } else if (game is won by playerOne){
//            return new ScoredMove(null, 1);
//        } else if (game is won by playerTwo){
//            return new ScoredMove(null, -1);
//        }
//
//        // we set scores initially out of range so as to ensure we will get a move
//        if (player is playerOne){
//            bestSoFar.score = -2;
//        } else{
//            bestSoFar.score = 2;
//        }
//
//        for each move m do:
//        perform m;
//        result = MiniMax(next player);
//        undo m;
//        if it is playerOne 's turn and the result is better than the bestSoFar then {
//            bestSoFar.move = m; // new best move
//            bestSoFar.score = result.score;
//        } else if (it is playerTwo's turn and the result is worse than the bestSoFar then) {
//            bestSoFar.move = m;
//            bestSoFar.score = result;
//        }
//
//        return bestSoFar;
//    }


    @Override
    public Move getNextMove(ArrayList<Move> possibleMoves) {
        return possibleMoves.get(0);
    }

    @Override
    public int getNext(ArrayList<Integer> possibleMoves) {

        System.out.println("\n\n");
        Token[] origBoard = new Token[9];
        for (int y = 0; y < board.getBoard().length; y++) {
            for (int x = 0; x < board.getBoard().length; x++) {
                origBoard[y * board.getBoard().length + x] = board.getBoard()[y][x];
            }
        }
        origBoard[0] = new Token(TokenState.CROSS);
        origBoard[1] = new Token(TokenState.EMPTY);
        origBoard[2] = new Token(TokenState.NOUGHT);

        origBoard[3] = new Token(TokenState.NOUGHT);
        origBoard[4] = new Token(TokenState.EMPTY);
        origBoard[5] = new Token(TokenState.CROSS);

        origBoard[6] = new Token(TokenState.EMPTY);
        origBoard[7] = new Token(TokenState.CROSS);
        origBoard[8] = new Token(TokenState.CROSS);

        System.out.println("Board:");
        for(int i = 0; i < origBoard.length; i++) {
            if (i % 3 == 0) {
                System.out.println();
            }
            System.out.print(" " + origBoard[i].toString());

        }
        System.out.println();

        ScoredMove bestSpot = Minimax(origBoard, playerOne, 0);

        System.out.println("index: " + bestSpot.index + " score: " + bestSpot.score + " position: " + bestSpot.position);
        System.out.println("Function calls: " + fc);

//        System.out.println("\n\n");
//        System.out.println("POSSIBLE MOVES");
//        for (Integer i : possibleMoves) {
//            System.out.print(" ( i : " + i + " ) ");
//        }
//        System.out.println("\n\n");
        return possibleMoves.get(0);
//        return bestSpot.position;
    }
}
