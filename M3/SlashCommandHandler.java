package M3;

/*
Challenge 2: Simple Slash Command Handler
-----------------------------------------
- Accept user input as slash commands
  - "/greet <name>" → Prints "Hello, <name>!"
  - "/roll <num>d<sides>" → Roll <num> dice with <sides> and returns a single outcome as "Rolled <num>d<sides> and got <result>!"
  - "/echo <message>" → Prints the message back
  - "/quit" → Exits the program
- Commands are case-insensitive
- Print an error for unrecognized commands
- Print errors for invalid command formats (when applicable)
- Capture 3 variations of each command except "/quit"
*/

import java.util.Scanner;

public class SlashCommandHandler extends BaseClass {
    private static String ucid = "Kar65"; // <-- change to your UCID
//Kar65 //October13
    public static void main(String[] args) {
        printHeader(ucid, 2, "Objective: Implement a simple slash command parser.");

        Scanner scanner = new Scanner(System.in);

        // Can define any variables needed here
        java.util.Random rng = new java.util.Random();

        while (true) {
            System.out.print("Enter command: ");
            String line = scanner.nextLine().trim();// get entered text
            if (line.isEmpty()) continue;

            String lower = line.toLowerCase();
// /quit
            if (lower.equals("/quit")) {
                System.out.println("Goodbye");
                break;
            }
            
            //// process echo

            else if (lower.startsWith("/greet ")) {
                String name = line.substring(7).trim();
                if (name.isEmpty()) {
                    System.out.println(" Format Error : Use /greet <name>");
                } else {
                    System.out.println("Hello, " + name + "!");
                }
            }

                // check if echo

            else if (lower.startsWith("/echo ")) {
                String msg = line.substring(6);
                if (msg.isBlank()) {
                    System.out.println ( " Format error: Use /echo <message>");
                } else {
                    System.out.println(msg);
                
                }
            }

            else if (lower.startsWith("/roll")) {
                String spec = line.substring(6).trim();
                int dPos = Math.max(spec.indexOf('d'), spec.indexOf('D'));

                if (dPos <= 0 || dPos >= spec.length() - 1) {
                    System.out.println("Format error: Use /roll <num>d<sides> (e.g., /roll 3d6)");
                } else {
                    String numText = spec.substring(0, dPos).trim();
                    String sidesText = spec.substring(dPos + 1).trim();
                    try {
                        int num = Integer.parseInt(numText);
                        int sides = Integer.parseInt(sidesText);

                        if (num <= 0 || sides <= 1) {
                            System.out.println ("Format error: num must be > 0 and sides must be > 1");
                        } else {
                          int sum = 0;
                          for (int i = 0; i < num; i++) {
                            sum += rng.nextInt(sides) + 1;
                            
                        }
                        System.out.println("Rolled " + num + "d" + sides +  " and got" + sum + "!");
                    }
                } catch (NumberFormatException e) {
                 System.out.println("Format error: Use intergers for <num> and <sides> (e.g., /roll 2d6)");
            
                 }
             }
        }

        else if (lower.startsWith("/")) {
            System.out.println("unkown command. Try /greet, /roll, /echo, or /quit.");
        }

        else {
            System.out.println("Commands must start with '/'.");
        }

        }

        printFooter(ucid, 2);
        scanner.close();
    }
}
