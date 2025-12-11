// package M4.Part2;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.Socket;
// import java.net.UnknownHostException;
// import java.util.Scanner;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// /**
//  * Demoing bi-directional communication between client and server
//  */
// public class Client {

//     private Socket server = null;
//     private PrintWriter out = null;
//     private BufferedReader in = null;
//     final Pattern ipAddressPattern = Pattern
//             .compile("/connect\\s+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{3,5})");
//     final Pattern localhostPattern = Pattern.compile("/connect\\s+(localhost:\\d{3,5})");
//     private boolean isRunning = false;

//     public Client() {
//         System.out.println("Client Created");
//     }

//     public boolean isConnected() {
//         if (server == null) {
//             return false;
//         }
//         // https://stackoverflow.com/a/10241044
//         // Note: these check the client's end of the socket connect; therefore they
//         // don't really help determine
//         // if the server had a problem and is just for lesson's sake
//         return server.isConnected() && !server.isClosed() && !server.isInputShutdown() && !server.isOutputShutdown();

//     }

//     /**
//      * Takes an ip address and a port to attempt a socket connection to a server.
//      * 
//      * @param address
//      * @param port
//      * @return true if connection was successful
//      */
//     private boolean connect(String address, int port) {
//         try {
//             server = new Socket(address, port);
//             // channel to send to server
//             out = new PrintWriter(server.getOutputStream(), true);
//             // channel to list to server
//             in = new BufferedReader(new InputStreamReader(server.getInputStream()));
//             System.out.println("Client connected");
//         } catch (UnknownHostException e) {
//             e.printStackTrace();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         return isConnected();
//     }

//     /**
//      * <p>
//      * Check if the string contains the <i>connect</i> command
//      * followed by an ip address and port or localhost and port.
//      * </p>
//      * <p>
//      * Example format: 123.123.123:3000
//      * </p>
//      * <p>
//      * Example format: localhost:3000
//      * </p>
//      * https://www.w3schools.com/java/java_regex.asp
//      * 
//      * @param text
//      * @return
//      */
//     private boolean isConnection(String text) {
//         // https://www.w3schools.com/java/java_regex.asp
//         Matcher ipMatcher = ipAddressPattern.matcher(text);
//         Matcher localhostMatcher = localhostPattern.matcher(text);
//         return ipMatcher.matches() || localhostMatcher.matches();
//     }

//     /**
//      * Controller for handling various text commands.
//      * <p>
//      * //KarenRalda //Kar65 //10/23/25
//      * </p>
//      * 
//      * @param text
//      * @return true if a text was a command or triggered a command
//      */
//     private boolean processClientCommand(String text) {
//         if (isConnection(text)) {
//             String[] parts = text.trim().replaceAll(" +", " ").split(" ")[1].split(":");
//             connect(parts[0].trim(), Integer.parseInt(parts[1].trim()));
//             return true;
//         } if ("/quit".equalsIgnoreCase(text)) { isrunning = false; return true; }

        
        
//         if (text !=null && text.startsWith("/pm ")) {
//             String[] p = text.trim().split("\\s"+ 3);
//              if (p.length < 3) {System.out.println("Usage: /pm <target id> <message>"); return true; }
//             if (!p[1]matches("\\d+")) { System.out.println("Target id must be a number."); return true; }      
//             if (!isConnected()) { System.out.println("Not connected to the server"); return true; }
// pm          out.println("/pm" + p[1] + " " + p[2]);
//             if (out.checkError()) System.out.println("Connection may be lost");
        
//              return true;
//         }
//         return false;
//     }

//     public void start() throws IOException {

//         System.out.println("Client starting");
//         try (Scanner si = new Scanner(System.in);) {
//             String line = "";
//             isRunning = true;
//             while (isRunning) {
//                 try {
//                     System.out.println("Waiting for input");
//                     line = si.nextLine();
//                     if (!processClientCommand(line)) {
//                         if (isConnected()) {
//                             out.println(line);
//                             // https://stackoverflow.com/a/8190411
//                             // you'll notice it triggers on the second request after server socket closes
//                             if (out.checkError()) {
//                                 System.out.println("Connection to server may have been lost");
//                             }
//                             // wait for reply
//                             // Note: now that we're attempting a read
//                             // we'll immediately get notified if the server's connection closes
//                             // Note2: if the server terminates before we send a message, client will exit
//                             // after the out.println() continues
//                             String fromServer = in.readLine();

//                             if (fromServer != null) {
//                                 System.out.println("Reply from server: " + fromServer);
//                             } else {
//                                 System.out.println("Server disconnected");
//                                 break;
//                             }
//                         } else {
//                             System.out.println("Not connected to server");
//                         }
//                     }
//                 } catch (Exception e) {
//                     System.out.println("Connection dropped");
//                     break;
//                 }
//             }
//             System.out.println("Exited loop");
//         } catch (Exception e) {
//             System.out.println("Exception from start()");
//             e.printStackTrace();
//         } finally {
//             close();
//         }
//     }

//     private void close() {
//         try {
//             System.out.println("Closing output stream");
//             out.close();
//         } catch (NullPointerException ne) {
//             System.out.println("Outputstream was never opened so this exception is ok");
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         try {
//             System.out.println("Closing input stream");
//             in.close();
//         } catch (NullPointerException ne) {
//             System.out.println("InputStream was never opened so this exception is ok");
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         try {
//             System.out.println("Closing connection");
//             server.close();
//             System.out.println("Closed socket");
//         } catch (IOException e) {
//             e.printStackTrace();
//         } catch (NullPointerException ne) {
//             System.out.println("Server was never opened so this exception is ok");
//         }
//     }

//     public static void main(String[] args) {
//         Client client = new Client();

//         try {
//             // if start is private, it's valid here since this main is part of the class
//             client.start();
//         } catch (IOException e) {
//             System.out.println("Exception from main()");
//             e.printStackTrace();
//         }
//     }
// }
