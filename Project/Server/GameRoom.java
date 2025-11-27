// Karen Ralda // kar65 // Nov 26 2025
package Project.Server;

import java.util.concurrent.ConcurrentHashMap;

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

    // Map of clientId -> their current choice ('r','p','s' or null)
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
    //Kar65 //KarenRalda //November25th2025
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

    //Karenralda //Kar65 //November262025
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
     * For now this is just a placeholder that would normally
     * resolve battles and decide if we need another round or to end the session.
     */
    public synchronized void onRoundEnd() {
        if (!sessionRunning) {
            return;
        }

        phase = Phase.RESOLVING;
        System.out.println("GameRoom[" + getName()
                + "] Round " + roundNumber + " ended, phase = RESOLVING");

        // In the full implementation you would:
        // - process battles
        // - give points
        // - mark eliminated players
        // - decide if we should call onSessionEnd() or start another round
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
}
