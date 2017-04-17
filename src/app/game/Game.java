package app.game;

import app.*;
import app.actors.Actor;
import app.actors.MiniMaxActor;
import app.gui.alerts.*;
import app.gui.dialogs.ConnectionDialog;
import app.network.CommandSender;
import app.network.Connection;
import app.utils.Debug;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import app.network.commands.*;
import app.gui.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joël Hoekstra
 */
public class Game {
    private GUI gui;
    private Board board;
    private GameType gameType;
    private ArrayList<Player> playerList;
    private ArrayList<Challenge> pendingChallenges;

    private Actor actor;
    private Match match = null;
    private App app;
    private boolean yourTurn = false;

    private boolean toUseAI = false;

    public Game(App app) {
        this.app = app;
        pendingChallenges = new ArrayList<>();
        board = new Board(gameType);
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
//        board = new Board(gameType); // TODO: 14/04/2017 create unique board types for tic tac toe and for reversi
        gui.reset();
        match = new Match(gameType, playerOne, playerTwo);
        match.start();
        update();
        gui.setLeftStatusText(getLoggedInPlayer().getUsername() + ", you have been placed in a new match. And you are " + playerNotice + " Good luck!");
    }

    public void placeMove(Move move) {
        if (isLoggedIn()) {
            if (match.canDoMove()) {
                match.addMove(move);
                board.addMove(move.getPosition(), match.getTokenByPlayer(move.getPlayer())); //move.getPlayer().getToken());// todo tijdelijke check..
                update();
            }
        }
        else {
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
        if(board.isValidMove(move, move.getPlayer().getToken())) {//match.getTokenByPlayer(move.getPlayer()))) {//move.getPlayer().getToken())) {
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

    public void handleMove(int movePosition) {
        Move move = new Move(movePosition, app.getUser());
        // TODO: 16-4-2017 finish and implement
        // If it's not your turn
        /** if(move.getPlayer() != this.playerToMove){
            Platform.runLater(() -> {
                Alert alert = new NotYourTurnAlert();
                alert.showAndWait();
            });
        } **/
        // If your move is valid
        if(board.isValidMove(move, move.getPlayer().getToken())) {//match.getTokenByPlayer(move.getPlayer()))) {
            CommandSender.addCommand(new MoveCommand(move));
            Debug.println("Nice one, valid move");
            gui.setLeftStatusText("Nice one, valid move!");
        } else { // If your move isn't valid
            Debug.println("Invalid move!");
            gui.setLeftStatusText("Invalid move!");
            Platform.runLater(() -> {
                Alert alert = new InvalidMoveAlert();
                alert.showAndWait();
            });
        }
    }

    // TODO: 16-4-2017 Finish and implement
    // TODO: 16/04/2017 MOVED TO BOARD but here for backwards compatibility.. DEPRECATED.
    public ArrayList<Integer> getPossibleMoves() {
        return board.getPossibleMoves(app.getUser());
//        ArrayList<Integer> possibleMoves = new ArrayList<>();
//        for (int y = 0; y < 8; y++) {
//            for (int x = 0; x < 8; x++) {
//                int position = (y * 8) + x;
//                Move move = new Move(position, app.getUser());
//                if (board.isValidMove(move, match.getTokenByPlayer(move.getPlayer()))) {
//                    possibleMoves.add(move.getPosition());
//                    // TODO: 16-4-2017 implement
//                    // board.getBoard()[y][x].setTokenState(TokenState.POSSIBLE);
//                }
//            }
//        }
//        Debug.println("Possible moves: " + Arrays.toString(possibleMoves.toArray()));
//        return possibleMoves;
    }

    public void update() {
        gui.update();
    }
}
