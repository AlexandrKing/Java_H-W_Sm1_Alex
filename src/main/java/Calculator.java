public class Calculator {
  public static void main(String[] args) {
    System.out.println("=== Simple Calculator ===");

    if (args.length == 3) {
      try {
        double a = Double.parseDouble(args[0]);
        double b = Double.parseDouble(args[2]);
        String operator = args[1];

        double result = 0;
        switch (operator) {
          case "+": result = a + b; break;
          case "-": result = a - b; break;
          case "*": result = a * b; break;
          case "/": result = a / b; break;
          default:
            System.out.println("Unknown operator: " + operator);
            return;
        }
        System.out.println(a + " " + operator + " " + b + " = " + result);

      } catch (NumberFormatException e) {
        System.out.println("Error: Please enter valid numbers!");
      }
    } else {
      System.out.println("Usage: java Calculator <number> <operator> <number>");
      System.out.println("Examples:");
      System.out.println("  java Calculator 5 + 3");
      System.out.println("  java Calculator 10.5 * 2");
    }
  }
}
