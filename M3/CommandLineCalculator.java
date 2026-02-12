package M3;

/*
Challenge 1: Command-Line Calculator
------------------------------------
- Accept two numbers and an operator as command-line arguments
- Supports addition (+) and subtraction (-)
- Allow integer and floating-point numbers
- Ensures correct decimal places in output based on input (e.g., 0.1 + 0.2 → 1 decimal place)
- Display an error for invalid inputs or unsupported operators
- Capture 5 variations of tests
*/

public class CommandLineCalculator extends BaseClass {
    private static String ucid = "Kar65"; // <-- change to your ucid

    public static void main(String[] args) {
        printHeader(ucid, 1, "Objective: Implement a calculator using command-line arguments.");

        if (args.length != 3) {
            System.out.println("Usage: java M3.CommandLineCalculator <num1> <operator> <num2>");
            printFooter(ucid, 1);
            return;
        }

        try {
            System.out.println("Calculating result...");
            //Kar65 //October 13th, 2025 
            //Integer numer the user inputs, operator, second number. Keeps them as strings
            String num1Text = args[0];
            String operator = args[1].trim();
            String num2Text= args[2];
// Allowing only + or -
            if (!operator.equals("+") && !operator.equals("-")) {
                 System.out.println("Unsupported operator: " + operator + ". Use + or - only.");
                 printFooter(ucid,1);
                 return;

            }
            // turning into real numbers 
            double a = Double.parseDouble(num1Text);
            double b = Double.parseDouble(num2Text);

            //decimals
            int decimals = Math.max(countDecimals(num1Text), countDecimals(num2Text));
            double result = operator.equals("+") ? a + b : a - b;

            System.out.printf("%s %s %s = %." + decimals + "f%n", num1Text, operator, num2Text, result);

            

        } catch (Exception e) {
            System.out.println("Invalid input. Please ensure correct format and valid numbers.");
        }

        printFooter(ucid, 1);
    }

    
     private static int countDecimals(String s) {
        int dot = s.indexOf('.');
        if (dot < 0) return 0;
        return s.length() - dot - 1;

    }
}
