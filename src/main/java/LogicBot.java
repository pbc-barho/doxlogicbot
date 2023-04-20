import generator.formula.FormulaGenerator;

/**
 * The main class of the logic bot.
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
