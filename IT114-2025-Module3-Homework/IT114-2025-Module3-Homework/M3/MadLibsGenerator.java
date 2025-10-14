package M3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
Challenge 3: Mad Libs Generator (Randomized Stories)
-----------------------------------------------------
- Load a **random** story from the "stories" folder
- Extract **each line** into a collection (i.e., ArrayList)
- Prompts user for each placeholder (i.e., <adjective>) 
    - Any word the user types is acceptable, no need to verify if it matches the placeholder type
    - Any placeholder with underscores should display with spaces instead
- Replace placeholders with user input (assign back to original slot in collection)
*/

public class MadLibsGenerator extends BaseClass {
    private static final String STORIES_FOLDER = "M3/stories";
    private static String ucid = "kar65"; // <-- change to your ucid

    public static void main(String[] args) {
        printHeader(ucid, 3,
                "Objective: Implement a Mad Libs generator that replaces placeholders dynamically.");

        Scanner scanner = new Scanner(System.in);
        File folder = new File(STORIES_FOLDER);

        if (!folder.exists() || !folder.isDirectory() || folder.listFiles().length == 0) {
            System.out.println("Error: No stories found in the 'stories' folder.");
            printFooter(ucid, 3);
            scanner.close();
            return;
        }
        List<String> lines = new ArrayList<>();
        // Start edits

        // load a random story file

        File[] allFiles + folder.listFiles();
        java.util.List<File> storyFiles = new java.util.ArrayList<>();
        if (allFiles != null) {
            for (File f : allFiles) {
                if (f.isFile()) storyFiles.add(f);
            }
        }

        // parse the story lines

        // iterate through the lines

        if (storyFiles.isEmpty()) {
            System,out.println("Error: No story found in " + STORIES_FOLDER);
            printFooter(ucid,3);
            scanner.close();
            return;
        }

        // prompt the user for each placeholder (note: there may be more than one
        // placeholder in a line)

        java.util.Random rng = new java.util.Random();
        File chosen = storyFiles.get(rng.nextInt(storyFiles.size()));
        System.out.println("loaded story file: " chosen.getName());

        //read 
        try (Scanner fileReader = new Scanner(chosen, "UTF-8"))) {
            while (fileReader.hasNextline()) {
               lines.add(fileReader.nextLine());
            }
        } catch (Exception e) {
           System.out.println("Error reading story file: " + e.getMessage());
           prrintFooter(ucid, 3);
           scanner.close();
           return;

    }
    
    java.util.LinkedHashSet<String> placeholders = new java.util.LinkedHashSet<>();

    for (String line : lines) {
        int start = 0;
        while (true) {
              int open = line.indexOf('<', startIndex);
              if (open == -1) break;
              int close = line.indexOf('>', open + 1);
              if (close == -1) break;
              String token = line.substring(open + 1. close); 
              placeholders.add(token);
              start= close + 1;
        }
    }

    //prompt for placeholder

    java.util.Map<String, String> answers = new java.util.HashMap<>;
    for (String token : placeholders) {
        String nice = token.replace('_' , ' ' );
        String answer = "";
        while (answer.isBlank()) {
            System.out.print("Enter a " + nice + ":");
            answer = scanner.nextLine().trim();
            if (answer.isBlamk()) {
                System.out.println("(Any word is fine;dont leave blank.))");

            }
        }
        answers.put(token,answer);
    }

    for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        for (java.util.Map.Entry<String, String> e : answers.entrySet()) {
            String token = e.getKey();
            String word = e.getValue();
            line = line.replace("<" + token + ">", word);

        }
        lines.set(i, line); 
    }




        // apply the update to the same collection slot

        // End edits
        System.out.println("\nYour Completed Mad Libs Story:\n");
        StringBuilder finalStory = new StringBuilder();
        for (String line : lines) {
            finalStory.append(line).append("\n");
        }
        System.out.println(finalStory.toString());

        printFooter(ucid, 3);
        scanner.close();
    }
}
