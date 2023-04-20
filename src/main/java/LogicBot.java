import generator.Formula.FormulaGenerator;

/**
 * The main class of the logic bot that is used to evaluate the tableau solver. This code does not publish the
 * tautologies on Twitter.
 */
public class LogicBot {

    /**
     * The main method that starts the formula generator.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        FormulaGenerator generator = new FormulaGenerator();
        Thread thread = new Thread(generator);
        thread.start();
    }
}
