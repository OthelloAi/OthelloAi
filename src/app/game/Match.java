package app.game;

import app.utils.Token;
import app.utils.TokenState;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class Match {

    private Game game;
    private GameType gameType;
    private Player playerOne;
    private Player playerTwo;

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
    }

    private void processMove(Move move) {
        moves.add(move);
        Player player = move.getPlayer();
        Board b = new Board(game.getBoardObj()); // clone board
        b.addMove(move.getPosition(), player.getToken()); // add move to cloned board
        boardStates.add(b);
        game.getBoardObj().setBoard(b.deepCopy(b.getBoard()));
        game.update();
    }

    public boolean gameOver(Board b) {
        if (gameState == GameState.LOSS || gameState == GameState.DRAW || gameState == GameState.WIN) {
            return true;
        }
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
        gameState = GameState.CONTINUE;
    }

    public void stop(GameState gameState) {
       this.gameState = gameState;
    }


    public void forfeit() {
        stop(GameState.LOSS);
    }

    public boolean isStarted() {
        return (gameState == GameState.CONTINUE);
    }
    public boolean isFinished() {
        return (gameState != GameState.CONTINUE);
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
