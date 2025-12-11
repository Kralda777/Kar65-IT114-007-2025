// package M4.Part2;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.util.HashMap;

// // KarenRalda // kar65 // 10.23.25
// // Simple server with private-message support using a map of clients
// public class Server {
//     private int port = 3000;

//     // Map of client id -> ServerThread so we can look up who to send PMs to
//    private final HashMap<ServerThread, Character> rpsChoice = new HashMap<>();

//     private void start(int port) {
//         this.port = port;
//         System.out.println("Listening on port " + this.port);

//         // Simple single-client echo/reverse server example
//         try (ServerSocket serverSocket = new ServerSocket(port);
//              Socket client = serverSocket.accept(); // blocking
//              PrintWriter out = new PrintWriter(client.getOutputStream(), true);
//              BufferedReader in = new BufferedReader(
//                      new InputStreamReader(client.getInputStream()))) {

//             System.out.println("Client connected, waiting for message");
//             String fromClient;
//             while ((fromClient = in.readLine()) != null) {
//                 System.out.println("From client: " + fromClient);

//                 if ("/kill server".equalsIgnoreCase(fromClient)) {
//                     System.out.println("Client killed server");
//                     break;
//                 } else if (fromClient.startsWith("/reverse")) {
//                     // Example: reverse the text after "/reverse "
//                     StringBuilder sb = new StringBuilder(fromClient.replace("/reverse ", ""));
//                     String rev = sb.reverse().toString();
//                     System.out.println("To client: " + rev);
//                     out.println(rev);
//                 } else {
//                     // Default: echo back what the client sent
//                     System.out.println("To client: " + fromClient);
//                     out.println(fromClient);
//                 }
//             }
//         } catch (IOException e) {
//             System.out.println("Exception from start()");
//             e.printStackTrace();
//         } finally {
//             System.out.println("closing server socket");
//         }
//     }

//     // Optional helpers (used by the rest of the project to register clients)
//     public void registerClient(int id, ServerThread thread) {
//         clients.put(id, thread);
//     }

//     public void unregisterClient(int id) {
//         clients.remove(id);
//     }

//     // kar65 // 10.23.25
//     // Sends a private message from one client id to another
//     public void sendPrivateMessage(int fromId, int toId, String message) {
//         ServerThread from = clients.get(fromId);
//         ServerThread to   = clients.get(toId);

//         // If we don't know who 'from' is, we can't send anything
//         if (from == null) {
//             return;
//         }

//         if (to != null && to != from) {
//             // Message to the receiver
//             to.sendToClient("PM from " + fromId + ": " + message);
//             // Confirmation to the sender
//             from.sendToClient("PM to " + toId + ": " + message);
//         } else {
//             // Receiver not found / offline
//             from.sendToClient("User " + toId + " is not online.");
//         }
//     }

//     public static void main(String[] args) {
//         System.out.println("Server Starting");
//         Server server = new Server();
//         int port = 3000;
//         try {
//             port = Integer.parseInt(args[0]);
//         } catch (Exception e) {
//             // will default to 3000
//         }
//         server.start(port);
//         System.out.println("Server Stopped");
//     }
// }

