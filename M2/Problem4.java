package M2;

public class Problem4 extends BaseClass {
    private static String[] array1 = { "hello world!", "java programming", "special@#$%^&characters", "numbers 123 456",
            "mIxEd CaSe InPut!" };
    private static String[] array2 = { "hello world", "java programming", "this is a title case test",
            "capitalize every word", "mixEd CASE input" };
    private static String[] array3 = { "  hello   world  ", "java    programming  ",
            "  extra    spaces  between   words   ",
            "      leading and trailing spaces      ", "multiple      spaces" };
    private static String[] array4 = { "hello world", "java programming", "short", "a", "even" };

    private static void transformText(String[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printArrayInfoBasic(arr, arrayNumber);

        // Challenge 1: Remove non-alphanumeric characters except spaces
        // Challenge 2: Convert text to Title Case
        // Challenge 3: Trim leading/trailing spaces and remove duplicate spaces
        // Result 1-3: Assign final phrase to `placeholderForModifiedPhrase`
        // Challenge 4 (extra credit): Extract up to middle 3 characters when possible (beginning starts at middle of phrase excluding the first and last characters),
        // assign to 'placeholderForMiddleCharacters'
        
        // if not enough characters assign "Not enough characters"
 
        // Step 1: sketch out plan using comments (include ucid and date)
        // Step 2: Add/commit your outline of comments (required for full credit)
        // Step 3: Add code to solve the problem (add/commit as needed)
        String placeholderForModifiedPhrase = "";
        String placeholderForMiddleCharacters = "";
        
        for(int i = 0; i <arr.length; i++){
            // Start Solution Edits
            //Kar65 09.30.25
            //Step1:  Using replaceAll to remove non-alphaanumeric chracters.(replaceAll("[A-za-z0-9 ]", "")) except spaces.
            //Step2: Using String methods to make title case.
            //Step3:Using trip to remove spaces at beginning and end,  using replaceALl to remove duplicate entries.
            //Step4:Assigning result to placeholderForModifiedPhrase
            //Step5:Using length() to determine middle
            //Step6: Using substring to get up to the middle characters
            //Step7: Using index check to ensure the middle chracters exlude first and last of word/phrase.
            //Step8: Asigning to placeholderForMiddleCharacters


            String original = String.valueOf(arr[i]); 
                String cleaned = original.replaceAll("[^A-Za-z0-9 ]", ""); //removing non-aplhanumerics, kept the spaces
                cleaned = cleaned.trim().replaceAll("\\s+",""); //trimmed


                String[] words = cleaned.isEmpty() ? new String[0] : cleaned.split("");
                StringBuilder titled = new StringBuilder(); //Titlecase
                for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
                    String word = words [wordIndex];
                    if (word.isEmpty()) continue;
                    char first = Character.toUpperCase(word.charAt(0));
                    String rest = (word.length() > 1 ) ? word.substring(1).toLowerCase() : "";
                    if (wordIndex > 0) titled.append(" ");
                    titled.append(first).append(rest);
                    titled.append(first).append(rest);
                }


                placeholderForModifiedPhrase = titled.toString(); //Step4

                //Challenge 4

                String s = placeholderForModifiedPhrase;
                if(s.length() <= 2) {
                    placeholderForMiddleCharacters = " Not enough characters";
                } else{
                    int n = s.length();
                    int start = n/2; 
                    if (start ==0) start = 1; //Skipping first character
                    if (start >= n -1) start = n- 2; // Skipping last character
                    int end = Math.min(start + 3, n - 1);
                    if (end <= start) {
                        placeholderForMiddleCharacters = "Not enough characters";
                    } else {
                        placeholderForMiddleCharacters = s.substring(start, end);
                    }
            }



             // End Solution Edits
            System.out.println(String.format("Index[%d] \"%s\" | Middle: \"%s\"",i, placeholderForModifiedPhrase, placeholderForMiddleCharacters));
        }

       

        
        System.out.println("\n______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "Kar65"; // <-- change to your UCID
        // No edits below this line
        printHeader(ucid, 4);

        transformText(array1, 1);
        transformText(array2, 2);
        transformText(array3, 3);
        transformText(array4, 4);
        printFooter(ucid, 4);
    }

}