package solver;

import generator.Formula.Agent;
import generator.Formula.Formula;
import generator.Formula.FormulaGenerator;

import java.util.ArrayList;

import generator.connectives.unary.Negation;
import io.Serializer;
import lombok.Getter;

/**
 * A class implementing the tableau solver.
 */
@Getter
public class TableauSolver implements Runnable {

    private final FormulaGenerator generator;

    /**
     * Constructor of the tableau solver.
     * @param generator The formula generator that generates the formulas that should be solved.
     */
    public TableauSolver(FormulaGenerator generator) {
        this.generator = generator;
    }

    /**
     * The main method of the tableau solver. It runs until all the formulas that were generated are solved
     * (with up to 6 connectives). It measures the run time and RAM usage per formula and stores the information in a
     * csv-file.
     */
    @Override
    public void run() {
        for (int complexity = 1; complexity < 7; complexity++) {
            boolean formulasToSolve = checkFormulas();
            Serializer.makeFile(complexity + "_nrcon.csv");
            while (formulasToSolve) {
                Formula currentFormula = runFile(complexity);
                if (currentFormula != null) {
                    long startTime = System.nanoTime();
                    solveTableau(currentFormula);
                    Runtime runtime = Runtime.getRuntime();
                    runtime.gc();
                    long memory = runtime.totalMemory() - runtime.freeMemory();
                    long endTime = System.nanoTime();
                    saveData(currentFormula, startTime, endTime, memory, currentFormula.getStatus());
                    currentFormula.getAgents().forEach(Agent::reset);
                    formulasToSolve = checkFormulas();
                } else {
                    break;
                }
            }
            Serializer.closeFile();
        }
    }

    /**
     * A method that calculates the run time of the tableau solver when validating a formula and saves the data per
     * formula to the CSV-file.
     * @param formula The formula that got solved.
     * @param startTime The starting time of the tableau solver.
     * @param endTime The ending time of the tableau solver.
     * @param memory The RAM usage of the tableau solver during the evaluation of the formula.
     * @param status The status indicating whether or not the formula is a tautology.
     */
    private void saveData(Formula formula, long startTime, long endTime, long memory, int status) {
        long execTime = endTime - startTime;
        String formulaString = formula.getFormulaTree().getString();
        boolean tautology = status != 1;
        Serializer.saveFormulaData(formulaString, execTime, memory, tautology);
    }

    /**
     * This method loads a file of a certain complexity and removes the first formula on it. This formula will be
     * solved. If the file is empty the method returns null.
     * @param i The complexity of the formula that should be solved.
     * @return The first formula on the file or null.
     */
    private Formula runFile(int i) {
        ArrayList<Formula> formulas = Serializer.loadFormulas(i + "_nr_connectives_solve.ser");
        if(formulas == null || formulas.size() == 0) {
            return null;
        }
        Formula currentFormula = formulas.get(0);
        formulas.remove(0);
        Serializer.saveFormulas(formulas, i + "_nr_connectives_solve.ser");
        return currentFormula;
    }

    /**
     * A method that checks whether there are still formulas that have been generated but not solved yet.
     * @return A boolean variable indicating whether there are formulas left.
     */
    private boolean checkFormulas() {
        int maxConnectives = 1;
        for (int i = 0; i < maxConnectives; i ++) {
            String fileToRun = i + "_nr_connectives_solve.ser";
            ArrayList<Formula> formulas = Serializer.loadFormulas(fileToRun);
            if (formulas != null) {
                if (formulas.size() != 0 ) {
                    return true;
                }
            } else {
                return false;
            }
            maxConnectives++;
        }
        return false;
    }

    /**
     * This method starts a tableau by negating the formula that should be evaluated. After the negated formula was
     * evaluated the status of the original formula gets adapted.
     * @param formula The formula that should be evaluated on whether it is a tautology.
     */
    private void solveTableau(Formula formula) {
        Formula negatedFormula = new Formula(new Negation(), formula);
        new Tableau(negatedFormula);
        formula.setStatus(negatedFormula.getStatus());
    }
}
