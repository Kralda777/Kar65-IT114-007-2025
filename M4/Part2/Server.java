package M4.Part2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import  java.util.HashMap;

public class Server {
    private int port = 3000;
//KarenRalda //Kar65 //10.23.25
private final HashMap<Integer, ServerThread> clients = new HashMap<>();
    private void start(int port) {
        this.port = port;
        System.out.println("Listening on port " + this.port);
        // server listening
        try (ServerSocket serverSocket = new ServerSocket(port);
                // client wait
                Socket client = serverSocket.accept(); // blocking;
                // send to client
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                // read from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {

            System.out.println("Client connected, waiting for message");
            String fromClient = "";
            while ((fromClient = in.readLine()) != null) {
                System.out.println("From client: " + fromClient);
                if ("/kill server".equalsIgnoreCase(fromClient)) {
                    // normally you wouldn't have a remote kill command, this is just for example
                    // sake
                    System.out.println("Client killed server");
                    break;
                } else if (fromClient.startsWith("/reverse")) {
             
                    // another example of server-side command
                    // Note: In the future command format processing will be client-side
                    // then client will send just the necessary data to the server so the server
                    // doesn't need to do as much string processing
                    StringBuilder sb = new StringBuilder(fromClient.replace("/reverse ", ""));
                    sb.reverse();
                    String rev = sb.toString();
                    System.out.println("To client: " + rev);
                    out.println(rev);
                } else {
                    System.out.println("To client: " + fromClient);
                    out.println(fromClient);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception from start()");
            e.printStackTrace();
        } finally {
            System.out.println("closing server socket");
        }
    }

    public void sendPrivateMessage(int fromId, int toId, String message) {
        ServerThread from = clients.get(fromId);
        ServerThread to  = clients.get(toId);

        String line = "PM from " + fromId + ":" + message; //format

        if (from != null) { from.sendToClient(line); }
        if (to != null && to !=from) { to.sendToClient(line); }

        if (to ==null && from != null) {
            from.send("User " + toId + " is not online. ");
        }
    }

    public static void main(String[] args) {
        System.out.println("Server Starting");
        Server server = new Server();
        int port = 3000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // can ignore, will either be index out of bounds or type mismatch
            // will default to the defined value prior to the try/catch
        }
        server.start(port);
        System.out.println("Server Stopped");
    }
}