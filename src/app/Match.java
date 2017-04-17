package app;

import app.game.GameState;
import app.game.GameType;
import app.game.Move;
import app.game.Player;
import app.utils.Debug;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class Match {

    private Player playerOne;
    private Player playerTwo;
    private Player winner;
    private Player activePlayer;

    private GameType gameType;

    private boolean started = false;
    private boolean finished = false;
    private boolean forfeited = false;

    private GameState gameState;

    private ArrayList<Move> moves;

    public Match(GameType gameType, Player playerOne, Player playerTwo) {
        this.gameType = gameType;
        moves = new ArrayList<>();
        playerOne.setOpponent(playerTwo);
        addPlayerOne(playerOne);
        playerOne.setToken(getTokenByPlayer(playerOne));

        playerTwo.setOpponent(playerOne);
        addPlayerTwo(playerTwo);
        playerTwo.setToken(getTokenByPlayer(playerTwo));

        activePlayer = playerOne;
    }

    public void addMove(Move move) {
        moves.add(move);
        Debug.println("Player: " + move.getPlayer().getUsername() + " made move: " + move.getPosition());
        if (playerOne.getUsername().equals(move.getPlayer().getUsername())) {
            activePlayer = playerTwo;
        } else if (playerTwo.getUsername().equals(move.getPlayer().getUsername())) {
            activePlayer = playerOne;
        }
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
        started = true;
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
