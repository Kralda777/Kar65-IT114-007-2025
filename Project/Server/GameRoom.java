// Karen Ralda // kar65 // Nov 26 2025
package Project.Server;

import java.util.concurrent.ConcurrentHashMap;

// If this import causes an error like "package Project.Server.unused does not exist",
// comment it out and make sure you have a Room class in Project.Server instead.

/**
 * GameRoom is a Room that knows about the RPS game state.
 * It tracks the session lifecycle and the current round.
 */
public class GameRoom extends Room {

    // Simple phase enum for the game lifecycle
    private enum Phase {
        WAITING,      // no session running yet
        CHOOSING,     // players are choosing r/p/s
        RESOLVING,    // round is being processed
        GAME_OVER     // session has ended
    }

    private Phase phase = Phase.WAITING;
    private int roundNumber = 0;

    // Map of clientId -> their current choice ('r','p','s','l','k' or null)
    private final ConcurrentHashMap<Long, Character> choices = new ConcurrentHashMap<>();

    // flag to indicate an active session
    private boolean sessionRunning = false;

    public GameRoom(String name) {
        super(name);
    }

    /**
     * onSessionStart()
     * Called when a new game session is started.
     * Resets all state and immediately triggers the first round.
     */
    // Kar65 // KarenRalda // November25th2025
    public synchronized void onSessionStart() {
        // reset overall game/session state
        sessionRunning = true;
        roundNumber = 0;
        choices.clear();
        phase = Phase.WAITING;

        // start the first round
        onRoundStart();
    }

    /**
     * onRoundStart()
     * Sets up a new round: clears previous choices and puts the room
     * into the "choosing" phase.
     */
    // Karenralda // Kar65 // November262025
    public synchronized void onRoundStart() {
        if (!sessionRunning) {
            return;
        }
        roundNumber++;

        // clear any old choices for active players
        choices.replaceAll((id, oldChoice) -> null);

        // set phase to choosing so clients can /pick
        phase = Phase.CHOOSING;

        System.out.println("GameRoom[" + getName()
                + "] Round " + roundNumber + " started, phase = CHOOSING");
    }

    /**
     * onRoundEnd()
     * Called when a round finishes. For now it just logs the phase change.
     * In a full implementation you would resolve battles, award points,
     * mark eliminated players, and decide whether to start another round.
     */
    public synchronized void onRoundEnd() {
        if (!sessionRunning) {
            return;
        }

        phase = Phase.RESOLVING;
        System.out.println("GameRoom[" + getName()
                + "] Round " + roundNumber + " ended, phase = RESOLVING");
    }

    /**
     * onSessionEnd()
     * Cleans up the game session and resets the GameRoom back to the waiting state.
     */
    public synchronized void onSessionEnd() {
        if (!sessionRunning) {
            return;
        }

        sessionRunning = false;
        phase = Phase.GAME_OVER;

        // reset any per-session data
        choices.clear();

        System.out.println("GameRoom[" + getName()
                + "] Session ended, phase = GAME_OVER");

        // After this, the next game would call onSessionStart() again.
    }

    private void broadcastBattleResult(long attackerId,
                                       long defenderId,
                                       char attackerChoice,
                                       char defenderChoice,
                                       long winnerId) {
        String msg = getPlayerName(attackerId) + " (" + attackerChoice + ") vs "
                   + getPlayerName(defenderId) + " (" + defenderChoice + ") \u2192 "
                   + getPlayerName(winnerId) + " wins";
        System.out.println("[GAME EVENT] " + msg);
        broadcastGameEvent(msg);
    }

    /**
     * Called when a player picks their choice.
     * Karenralda // Kar65 // December082025
     */
    private void broadcastPickedChoice(long playerId, char choice) {
        String msg = getPlayerName(playerId) + " picked their choice.";
        System.out.println("[GAME EVENT] " + msg);
        broadcastGameEvent(msg);
    }

    /**
     * Called when a player is eliminated from the session.
     */
    private void broadcastElimination(long playerId) {
        String msg = getPlayerName(playerId) + " has been eliminated.";
        System.out.println("[GAME EVENT] " + msg);
        broadcastGameEvent(msg);
    }

    private void broadcastGameEvent(String msg) {
        // For now we only log to console; the server/client pipeline
        // (ServerThread + ClientUI) would surface this in the UI.
        System.out.println("[BROADCAST GAME EVENT] " + msg);
    }

    /**
     * Simple helper to get a displayable name from a player id.
     */
    private String getPlayerName(long playerId) {
        // Placeholder for screenshot / demonstration purposes
        return "Player " + playerId;
    }
}
