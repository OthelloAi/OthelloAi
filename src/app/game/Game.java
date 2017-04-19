package app.game;

import app.*;
import app.actors.Actor;
import app.actors.IterativeActor;
import app.actors.MiniMaxActor;
import app.actors.RandomActor;
import app.gui.alerts.*;
import app.network.CommandSender;
import app.utils.ActorState;
import app.utils.Debug;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import app.network.commands.*;
import app.gui.GUI;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Joël Hoekstra
 */
public class Game {
    private GUI gui;
    private Board board;
    private GameType gameType;
    private ArrayList<Challenge> pendingChallenges;
    private ArrayList<Integer> movesMade;
    private Stack<Pair<Move, Board>> boardStates;
    private ActorState actorState = ActorState.HUMAN;
    private Actor actor;
    private Match match = null;
    private App app;
    private boolean yourTurn = false;

    private boolean toUseAI = false;

    public Game(App app) {
        this.app = app;
        boardStates = new Stack<>();
        movesMade = new ArrayList<>();
        pendingChallenges = new ArrayList<>();
        board = new Board(gameType);
        actorState = ActorState.MINIMAX;
        actor = new MiniMaxActor(this, board);
        Debug.println("I am debugging now <3");
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void addGUI(GUI gui) {
        this.gui = gui;
    }

    public void useAI(boolean toUseAI) {
        this.toUseAI = toUseAI;
    }

    public boolean usesAI() {
        return toUseAI;
    }

    public ActorState getActorState() {
        return this.actorState;
    }

    public Actor getActor() {
        return actor;
    }
    public Match getMatch() {
        return match;
    }
    public GameType getGameType() {
        return gameType;
    }


    public void setGameType(GameType gameType) {
        this.gameType = gameType;
        board = new Board(gameType);
        gui.render();
    }

    public Player getLoggedInPlayer() {
        return app.getUser();
    }

    public boolean isInMatch() {
        return (match != null && match.isStarted() && !match.isFinished());
    }

    public int getScore(Player player) {
        int score = 0;
        Token[][] b = board.getBoard();
        for (int y = 0; y < b.length; y++) {
            for (int x = 0; x < b.length; x++) {
                if (board.getBoard()[y][x].getState() == player.getToken().getState()) {
                    score++;
                }
            }
        }
        return score;
    }

    public Token[][] getBoard() {
        return board.getBoard();
    }
    public Board getBoardObj() {
        return board;
    }

    public void addPendingChallenge(Challenge challenge) {
        this.pendingChallenges.add(challenge);
        Platform.runLater(() -> {
            AcceptDeclineAlert dialog = new AcceptDeclineAlert(challenge);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == ButtonType.OK) {
                CommandSender.addCommand(new ChallengeAcceptCommand(challenge));
            } else {
                this.pendingChallenges.remove(challenge);
            }
        });
    }

    public void setActorState(String Actor) {
        switch (Actor) {
            case "MiniMax" :
                this.actor = new MiniMaxActor(this, board);
                this.actorState = ActorState.MINIMAX;
                break;
            case "Iterative" :
                this.actor = new IterativeActor();
                this.actorState = ActorState.ITERATIVE;
                break;
            case "Random" :
                this.actor = new RandomActor();
                this.actorState = ActorState.RANDOM;
                break;
            case "Human" :
                this.actor = new MiniMaxActor(this, board);
                this.actorState = ActorState.HUMAN;
        }
    }
    public Match endMatch(GameState gameState) {
        gui.setLeftStatusText("Match has ended.. Thanks for playing. " + gameState.name());
        match.stop(gameState);
        return match;
    }

    public boolean isLoggedIn() {
        return (app.getUser() != null);
    }

    public void startMatch(Player playerOne, Player playerTwo, GameType gameType) {
        if (actorState == ActorState.HUMAN) {
            useAI(false);
        } else {
            useAI(true);
        }
        playerOne.setOpponent(playerTwo);
        playerTwo.setOpponent(playerOne);

        String playerNotice;
        if (getLoggedInPlayer().getUsername().equals(playerOne.getUsername())) {
            playerNotice = "Player one -> "  + TokenState.BLACK.toString();
            Debug.println("You are player one with token " + TokenState.BLACK.toString());
        } else {
            playerNotice = "Player two -> "  + TokenState.WHITE.toString();
            Debug.println("You are player two with token " + TokenState.WHITE.toString());
        }

        Debug.println(getLoggedInPlayer().getUsername() + " you're placed in a new match..");
        Platform.runLater(() -> {
            StartMatchAlert startMatchAlert = new StartMatchAlert();
            startMatchAlert.showAndWait();
        });
        setGameType(gameType);
        gui.reset();
        match = new Match(this, gameType, playerOne, playerTwo);
        match.start();
        update();
        gui.setLeftStatusText(getLoggedInPlayer().getUsername() + ", you have been placed in a new match. And you are " + playerNotice + " Good luck!");
    }

    public void sendAIMove() {
        if (!match.gameOver(board)) {
            if (usesAI()) {
                ArrayList<Integer> possibleMoves = getPossibleMoves();
                if (possibleMoves.size() > 0) {
                    CommandSender.addCommand(new AIMoveCommand(this));
                }
            }
        }
    }

    public void processMove(Move move) {
        if (match != null) {
            boardStates.push(new Pair(move, new Board(board)));
            match.addMove(move);
            movesMade.add(move.getPosition());
        }
        update();
    }
    
    public void forfeit() {
        match.forfeit();
        match.stop(GameState.LOSS);
        gui.setLeftStatusText("You have forfeited the match.");
        update();
    }

    public void handleMove(Move move) {
        // If your move is valid
        if(board.isValidMove(move, move.getPlayer().getToken())) {
            CommandSender.addCommand(new MoveCommand(move));
            setYourTurn(false);
            gui.setLeftStatusText("Nice one, valid move!");
        } else { // If your move isn't valid
            gui.setLeftStatusText("Invalid move!");
            Platform.runLater(() -> {
                Alert alert = new InvalidMoveAlert();
                alert.showAndWait();
            });
        }
    }

    public ArrayList<Integer> getPossibleMoves() {
        return board.getPossibleMoves(app.getUser());
    }

    // help = hints
    public void showHelp() {
        removeHelp();
        if (isYourTurn() && !usesAI()) {
            for (int pos : getPossibleMoves()) {
                int posY = pos / getBoard().length;
                int posX = pos % getBoard().length;
                if (pos == actor.getNext(getPossibleMoves())) {
                    getBoard()[posY][posX] = new Token(TokenState.BEST);
                } else {
                    getBoard()[posY][posX] = new Token(TokenState.POSSIBLE);
                }
            }
        }
        update();
    }

    // help = hints
    public void removeHelp() {
        for (int y = 0; y < getBoard().length; y++) {
             for (int x = 0; x < getBoard().length; x++) {
                 if (getBoard()[y][x].getState() == TokenState.POSSIBLE || getBoard()[y][x].getState() == TokenState.BEST) {
                     getBoard()[y][x] = new Token(TokenState.EMPTY);
                 }
             }
        }
        update();
    }

    public void update() {
        gui.update();
    }

}
