package Project.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import Project.Common.PayloadType;
import Project.Common.PointsPayload;

public class Server {
    private int port = 3000;
    // connected clients
    // Use ConcurrentHashMap for thread-safe client management
    // the Long will be a unique client identifier, and ServerThread is the instance
    //KarenRalda //Kar65 //Nov3rd,2025  
    private final ConcurrentHashMap<Long, ServerThread> connectedClients = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CopyOnWriteArraySet<ServerThread>> rooms = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ServerThread, String> clientRoom = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<ServerThread, Integer> rpsPoints = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ServerThread, Character> rpsChoice = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<ServerThread, Boolean> rpsEliminated = new ConcurrentHashMap<>();
    private boolean rpsRoundActive = false;
    private long rpsRoundStartTime = 0L;

    private void initLobby() {
        rooms.computeIfAbsent("lobby", k -> new CopyOnWriteArraySet<>());
    }

    private boolean isRunning = true;

    private void roomLog(String msg) {
        System.out.println("[ROOM]" + msg);
    }

    private void start(int port) {
        this.port = port;
        // server listening
        System.out.println("Listening on port " + this.port);
        roomLog("default lobby ready");
        initLobby();

        // Simplified client connection loop
        //KarenRalda //Kar65 //11/03/25
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (isRunning) {
                System.out.println("Waiting for next client");
                Socket incomingClient = serverSocket.accept(); // blocking action, waits for a client connection
                System.out.println("Client connected");
                // wrap socket in a ServerThread, pass a callback to notify the Server when
                // they're initialized
                ServerThread serverThread = new ServerThread(incomingClient, this, this::onServerThreadInitialized);
                // start the thread (typically an external entity manages the lifecycle and we
                // don't have the thread start itself)
                serverThread.start();
                // Note: We don't yet add the ServerThread reference to our connectedClients map
            }
        } catch (IOException e) {
            System.err.println("Error accepting connection");
            e.printStackTrace();
        } finally {
            System.out.println("Closing server socket");
        }
    }

    //Karen Ralda //Kar65 //11/04/25
    /**
     * Callback passed to ServerThread to inform Server they're ready to receive
     * data
     * 
     * @param serverThread
     */
    private void onServerThreadInitialized(ServerThread serverThread) {
        initLobby();
        rooms.get("lobby").add(serverThread);
        clientRoom.put(serverThread, "lobby");

        roomLog(serverThread.getClientId() + "initialized; places in lobby");
        // add to connected clients list (unique id and actual reference)
        connectedClients.put(serverThread.getClientId(), serverThread);
        relay(null, String.format("*User[%s] connected*", serverThread.getClientId()));

        rpsPoints.put(serverThread, 0);
        rpsEliminated.put(serverThread, false);
        rpsChoice.put(serverThread, null);
    }

    /**
     * Takes a ServerThread and removes them from the Server
     * Adding the synchronized keyword ensures that only one thread can execute
     * these methods at a time,
     * preventing concurrent modification issues and ensuring thread safety
     * 
     * @param serverThread
     */
    private synchronized void disconnect(ServerThread serverThread) {
        serverThread.disconnect();
        // remove disconnecting ServerThread from map
        ServerThread disconnectingServerThread = connectedClients.remove(serverThread.getClientId());
        if (disconnectingServerThread != null) {
            roomLog(disconnectingServerThread.getClientId() + " left loy");
            // Improved logging with user ID
            relay(null, "User[" + disconnectingServerThread.getClientId() + "] disconnected");

            rpsPoints.remove(disconnectingServerThread);
            rpsChoice.remove(disconnectingServerThread);
            rpsEliminated.remove(disconnectingServerThread);
        }
    }

    /*
     * Relays the message from the sender to all connectedClients
     * Internally calls processCommand and evaluates as necessary.
     * Note: Clients that fail to receive a message get removed from
     * connectedClients.
     * Adding the synchronized keyword ensures that only one thread can execute
     * these methods at a time,
     * preventing concurrent modification issues and ensuring thread safety
     * 
     * @param message
     * @param sender ServerThread (client) sending the message or null if it's a
     *                server-generated message
     */
    private synchronized void relay(ServerThread sender, String message) {
        String senderString = sender == null ? "Server" : String.format("User[%s]", sender.getClientId());
        final String formattedMessage = String.format("%s: %s", senderString, message);

        connectedClients.values().removeIf(serverThread -> {
            boolean failedToSend = !serverThread.sendToClient(formattedMessage);
            if (failedToSend) {
                System.out.println(
                        String.format("Removing disconnected client[%s] from list", serverThread.getClientId()));
                disconnect(serverThread);
            }
            return failedToSend;
        });
    }

    // start handle actions
    protected synchronized void handleDisconnect(ServerThread sender) {
        disconnect(sender);
    }

    protected synchronized void handleReverseText(ServerThread sender, String text) {
        StringBuilder sb = new StringBuilder(text);
        sb.reverse();
        String rev = sb.toString();
        relay(sender, rev);
    }

    protected synchronized void flipCoin(ServerThread sender) {
        String who = "User[" + sender.getClientId() + "]";
        String result = (Math.random() < 0.5) ? "Heads" : "Tails";
        relay(null, String.format("%s flipped a coin and got %s", who, result));
    }

    public synchronized void sendPrivateMessage(ServerThread from, long toId, String msg) {
        if (from == null) return;

        ServerThread to = connectedClients.get(toId);
        if (to != null && to != from) {
            to.sendToClient("PM from " + from.getClientId() + ":" + msg);
            from.sendToClient("PM to " + toId + ": " + msg);
        } else {
            from.sendToClient("User " + toId + " is not online.");
        }
    }

    protected synchronized void handleMessage(ServerThread sender, String text) {
        relay(sender, text);
    }

    protected synchronized void createRoom(String name) {
        if (name == null || name.isBlank() || "lobby".equalsIgnoreCase(name)) return;
        rooms.computeIfAbsent(name, k -> new CopyOnWriteArraySet<>());
        roomLog("created room " + name);
    }

    protected synchronized void joinRoom(ServerThread who, String name) {
        if (who == null || name == null || name.isBlank()) return;
        rooms.computeIfAbsent(name, k -> new CopyOnWriteArraySet<>());

        String prev = clientRoom.get(who);
        if (prev != null) {
            var set = rooms.get(prev);
            if (set != null) {
                set.remove(who);
                roomLog(who.getClientId() + "left " + prev);
                removeRoomIfEmpty(prev);
            }
        }

        rooms.get(name).add(who);
        clientRoom.put(who, name);
        roomLog(who.getClientId() + "joined" + name);
    }

    protected synchronized void leaveRoom(ServerThread who) {
        if (who == null) return;

        String prev = clientRoom.get(who);
        if (prev != null && !"lobby".equals(prev)) {
            var set = rooms.get(prev);
            if (set != null) {
                set.remove(who);
                roomLog(who.getClientId() + " left " + prev);
                removeRoomIfEmpty(prev);
            }
        }
        rooms.get("lobby").add(who);
        clientRoom.put(who, "lobby");
        roomLog(who.getClientId() + "joined lobby");
    }

    private void removeRoomIfEmpty(String name) {
        if ("lobby".equals(name)) return;
        var set = rooms.get(name);
        if (set != null && set.isEmpty()) {
            rooms.remove(name);
            roomLog("removed room " + name);
        }
    }
    //Karenralda //Kar65 //November 2025

    // Milestone 2 – handle a player's rock/paper/scissors choice
    protected synchronized void receiveRPSChoice(ServerThread player, char choice) {
        if (player == null) {
            return;
        }

        // If eliminated, ignore their picks
        if (Boolean.TRUE.equals(rpsEliminated.get(player))) {
            player.sendToClient("You are eliminated and cannot pick.");
            return;
        }

        // Store this player's choice
        //Karenralda //Kar65 //November25th2025
        rpsChoice.put(player, choice);

        relay(null, "User[" + player.getClientId() + "] locked in their choice.");

        // Check if all ACTIVE (not eliminated) players have made a choice
        boolean allPicked = connectedClients.values().stream()
                .filter(c -> !Boolean.TRUE.equals(rpsEliminated.get(c)))
                .allMatch(c -> rpsChoice.get(c) != null);

        if (!allPicked) {
            return;
        }

        boolean hasR = false;
        boolean hasP = false;
        boolean hasS = false;

        for (ServerThread c : connectedClients.values()) {
            if (Boolean.TRUE.equals(rpsEliminated.get(c))) continue;
            Character ch = rpsChoice.get(c);
            if (ch == null) continue;

            if (ch == 'R') hasR = true;
            else if (ch == 'P') hasP = true;
            else if (ch == 'S') hasS = true;
        }

        Character winningChoice = null;

        // Determine winning choice
        //Kar65 //KarenRalda //November25th
        if (hasR && hasP && !hasS) {
            winningChoice = 'P';
        } else if (hasP && hasS && !hasR) {
            winningChoice = 'S';
        } else if (hasR && hasS && !hasP) {
            winningChoice = 'R';
        }

        if (winningChoice == null) {
            relay(null, "Round result: tie. No one is eliminated.");
        } else {
            String winLabel =
                    winningChoice == 'R' ? "ROCK" :
                    winningChoice == 'P' ? "PAPER" : "SCISSORS";

            StringBuilder winners = new StringBuilder();
            StringBuilder losers = new StringBuilder();

            for (ServerThread c : connectedClients.values()) {
                if (Boolean.TRUE.equals(rpsEliminated.get(c))) continue;
                Character ch = rpsChoice.get(c);
                if (ch == null) continue;

                if (ch == winningChoice) {
                    int old = rpsPoints.getOrDefault(c, 0);
                    int now = old + 1;
                    rpsPoints.put(c, now);
                    winners.append(c.getClientId()).append(" ");
                    c.sendToClient("[RPS] You win this round and now have " + now + " point(s).");
                } else {
                    rpsEliminated.put(c, true);
                    losers.append(c.getClientId()).append(" ");
                    c.sendToClient("[RPS] You lost this round and are eliminated.");
                }
            }

            relay(null, "Round result: " + winLabel + " wins. Winners: " + winners + "Losers: " + losers);
        }
//KarenRalda //Kar65 //Nov252025
        // Clear choices for next round
        connectedClients.values().forEach(c -> rpsChoice.put(c, null));

        long remaining = connectedClients.values().stream()
                .filter(c -> !Boolean.TRUE.equals(rpsEliminated.get(c)))
                .count();

        if (remaining <= 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Game over. Final scores:\n");

            connectedClients.values().stream()
                    .sorted((a, b) -> Integer.compare(
                            rpsPoints.getOrDefault(b, 0),
                            rpsPoints.getOrDefault(a, 0)))
                    .forEach(c -> {
                        int pts = rpsPoints.getOrDefault(c, 0);
                        sb.append("User[").append(c.getClientId()).append("]: ")
                                .append(pts).append(" point(s)");
                        if (Boolean.TRUE.equals(rpsEliminated.get(c))) {
                            sb.append(" (eliminated)");
                        }
                        sb.append("\n");
                    });

            relay(null, sb.toString());

            connectedClients.values().forEach(c -> {
                rpsPoints.put(c, 0);
                rpsEliminated.put(c, false);
                rpsChoice.put(c, null);
            });

            relay(null, "RPS session reset. Everyone can /pick again to start a new game.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Server Starting");
        Server server = new Server();
        int port = 3000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // ignore
        }
        server.start(port);
        System.out.println("Server Stopped");
    }
}
