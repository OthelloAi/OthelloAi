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

import java.util.*;

/**
 * @author Joël Hoekstra
 */
public class Game {
    private GUI gui;
    private Board board;
    private GameType gameType;
    private ArrayList<Player> playerList;
    private ArrayList<Challenge> pendingChallenges;
    public static Stack<Move> moves;
    private ActorState actorState = ActorState.HUMAN;
    private Actor actor;
    private Match match = null;
    private App app;
    private boolean yourTurn = false;
    private boolean processingMove = false;
    private MoveHandler moveHandler;

    private boolean toUseAI = false;

    public Game(App app) {
        this.app = app;
        moves = new Stack<>();
        pendingChallenges = new ArrayList<>();
        board = new Board(gameType);
        actor = new MiniMaxActor(this, board);
        Debug.println("I am debugging now <3");

        moveHandler = new MoveHandler(this);
        Thread moveHandlerThread = new Thread(moveHandler);
        moveHandlerThread.setDaemon(true);
        moveHandlerThread.start();
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

    public ArrayList<Player> getPlayerList() {return playerList;}

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

    public void setPlayers(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPendingChallenge(Challenge challenge) {
        this.pendingChallenges.add(challenge);
        //TODO With @Martijn alerts toevoegen.
        // show dialog here
        Platform.runLater(() -> {
            AcceptDeclineAlert dialog = new AcceptDeclineAlert();
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
                this.actorState = ActorState.HUMAN;
        }
    }
    public Match endMatch(EndState endState) {

        gui.setLeftStatusText("Match has ended.. Thanks for playing. " + endState.name());
        // TODO: 14/04/2017 add functionality for ending a game here
//        match.stop();
        return match;
    }

    public boolean isLoggedIn() {
        return (app.getUser() != null);
    }

    public void startMatch(Player playerOne, Player playerTwo, GameType gameType) {
        playerOne.setOpponent(playerTwo);
        playerTwo.setOpponent(playerOne);
        MoveHandler.clear();
        processingMove = false;

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
        match = new Match(gameType, playerOne, playerTwo);
        match.start();
        update();
        gui.setLeftStatusText(getLoggedInPlayer().getUsername() + ", you have been placed in a new match. And you are " + playerNotice + " Good luck!");
    }

    public void placeMove(Move move) {
        MoveHandler.addMove(move);
        System.out.println("placeMove " + move.getPosition());
    }


    public boolean isProcessingMove() {
        return processingMove;
    }

    // used by the moveHandler thread
    public void processMove(Move move) {
        if (isLoggedIn()) {
            if (match.canDoMove()) {
                processingMove = true;
                match.addMove(move);
                board.addMove(move.getPosition(), move.getPlayer().getToken());
                if (gui.ifShowHelp) {
                    showHelp();
                }
                update();
                // if all is done send response
                processingMove = false;
                // get ai next move when the stack is empty
//                if (moves.empty()) {
//                    Move aiMove = new Move(actor.getNext(getPossibleMoves()), getLoggedInPlayer());
//                    CommandSender.addCommand(new MoveCommand(aiMove));
//                }
            }
        } else {
            // perhaps the reset should happen here.
            // or an empty game gui should be rendered.
        }

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

    // TODO: 16/04/2017 MOVED TO BOARD but here for backwards compatibility.. DEPRECATED.
    public ArrayList<Integer> getPossibleMoves() {
        return board.getPossibleMoves(app.getUser());
    }

    public void showHelp() {
        removeHelp();
        if (isYourTurn()) {
            for (int pos : getPossibleMoves()) {
                int posY = pos / getBoard().length;
                int posX = pos % getBoard().length;
                if (pos == actor.getNext(getPossibleMoves())) {
                    getBoard()[posY][posX] = new Token(TokenState.BEST);
                } else {
                    getBoard()[posY][posX] = new Token(TokenState.POSSIBLE);//.setTokenState(TokenState.POSSIBLE);
                }
            }
        }
    }

    public void removeHelp() {
        for (int y = 0; y < getBoard().length; y++) {
             for (int x = 0; x < getBoard().length; x++) {
                 if (getBoard()[y][x].getState() == TokenState.POSSIBLE) {
                     getBoard()[y][x] = new Token(TokenState.EMPTY);
                 }
             }
        }
    }

    public void update() {
        gui.update();
    }

}
