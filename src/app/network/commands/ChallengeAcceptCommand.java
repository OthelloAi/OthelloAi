package app.network.commands;

import app.network.Challenge;

/**
 * @author js
 */
public class ChallengeAcceptCommand implements Command {

    private Challenge challenge;

    public ChallengeAcceptCommand(Challenge challenge) {
        this.challenge = challenge;
    }

    @Override
    public String toString() {
        return "challenge accept " + challenge.getId();
    }
}
