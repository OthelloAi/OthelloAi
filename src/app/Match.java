package app;

import app.game.*;
import app.utils.Debug;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class Match {

    private Game game;
    private GameType gameType;
    private Player playerOne;
    private Player playerTwo;
    private Player winner;
    private Player activePlayer;
    private Player initialPlayer;

    private boolean started = false;
    private boolean finished = false;
    private boolean forfeited = false;

    private GameState gameState;

    private ArrayList<Move> moves;
    private ArrayList<Board> boardStates;

    public Match(Game game, GameType gameType, Player playerOne, Player playerTwo) {
        this.game = game;
        this.gameType = gameType;
        moves = new ArrayList<>();
        boardStates = new ArrayList<>();

        playerOne.setOpponent(playerTwo);
        addPlayerOne(playerOne);
        playerOne.setToken(getTokenByPlayer(playerOne));

        playerTwo.setOpponent(playerOne);
        addPlayerTwo(playerTwo);
        playerTwo.setToken(getTokenByPlayer(playerTwo));

        initialPlayer = playerOne;
    }

    private void printMovesList() {
        for (Move move : moves) {
            Debug.print(move.getPosition() + ", ");
        }
        Debug.println();
    }

    private void processMove(Move move) {
        moves.add(move);
//        printMovesList();
        Player player = move.getPlayer();
        Board b = new Board(game.getBoardObj()); // clone board
        b.addMove(move.getPosition(), player.getToken()); // add move to cloned board
        boardStates.add(b);
        game.getBoardObj().setBoard(b.deepCopy(b.getBoard()));//.addMove(move.getPosition(), player.getToken());
        game.update();
    }

    public boolean gameOver(Board b) {
        if (gameState == GameState.LOSS || gameState == GameState.DRAW || gameState == GameState.WIN) {
            return true;
        }
//        if (b.getPossibleMoves(playerOne).size() == 0 && b.getPossibleMoves(playerTwo).size() > 0) {
//            Debug.println("BOTH PLAYERS HAVE NO MOVES LEFT");
//            return true;
//        }
        return false;
    }

    public void addMove(Move move) {
        processMove(move);
    }

    public void addPlayerOne(Player player) {
        this.playerOne = player;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void addPlayerTwo(Player player) {
        this.playerTwo = player;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void start() {
        if (playerOne == null || playerTwo == null) {
            return;
        }
        activePlayer = playerOne;
        gameState = GameState.CONTINUE;
    }

    public void stop(GameState gameState) {
       this.gameState = gameState;
       finished = true;
    }

    public void addGameState(GameState gameState) {
        this.gameState = gameState;
        if (gameState == GameState.DRAW ||
                gameState == GameState.LOSS ||
                gameState == GameState.WIN) {
            finished = true;
        }
    }

    public void forfeit() {
        stop(GameState.LOSS);
        forfeited = true;
    }

    public boolean isStarted() {
        return (gameState == GameState.CONTINUE);
    }
    public boolean isFinished() {
        return (gameState != GameState.CONTINUE);
//        return finished;
    }
    public boolean canDoMove() {
        return (gameState == GameState.CONTINUE); // todo duplicate functionality.. CLEAN UP
    }

    public Token getTokenByPlayer(Player player) {
        if (player.getUsername().equals(getPlayerOne().getUsername())) {
            if (gameType == GameType.REVERSI) return new Token(TokenState.BLACK);
            if (gameType == GameType.TIC_TAC_TOE) return new Token(TokenState.CROSS);
        }

        if (player.getUsername().equals(getPlayerTwo().getUsername())) {
            if (gameType == GameType.REVERSI) return new Token(TokenState.WHITE);
            if (gameType == GameType.TIC_TAC_TOE) return new Token(TokenState.NOUGHT);
        }
        return new Token(TokenState.EMPTY);
    }
}
