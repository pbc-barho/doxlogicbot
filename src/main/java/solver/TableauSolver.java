package solver;

import generator.formula.Agent;
import generator.formula.Formula;
import generator.formula.FormulaGenerator;

import java.util.ArrayList;

import generator.connectives.unary.Negation;
import io.Publisher;
import io.Serializer;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * A class implementing the tableau solver.
 */
@Getter
public class TableauSolver implements Runnable {

    private final FormulaGenerator generator;

    private ArrayList<Formula> tautologies = new ArrayList<>();

    /**
     * Constructor of the tableau solver.
     * @param generator The formula generator that generates the formulas that should be solved.
     */
    public TableauSolver(FormulaGenerator generator) {
        this.generator = generator;
    }

    /**
     * The main method of the tableau solver. It runs when there are still formulas that can be evaluated and sleeps
     * otherwise. It chooses a random formula from the generated formulas and evaluates whether it is a tautology. If
     * that is the case it gets added to a tautology list. When the first tautology gets stored in that list, the
     * Twitter publisher gets started.
     */
    @SneakyThrows
    @Override
    public void run() {
        boolean formulasToSolve = checkFormulas();
        boolean posting = false;
        while (formulasToSolve) {
            Formula currentFormula = generator.chooseRandomFormula();
            if (currentFormula != null) {
                solveTableau(currentFormula);
                if (currentFormula.getStatus() == 2) tautologies.add(currentFormula);
                currentFormula.getAgents().forEach(Agent::reset);
                if (tautologies.size() > 0 && !posting) {
                    posting = true;
                    startPublisher();
                }
            } else {
                Thread.sleep(3600000);
            }
            formulasToSolve = checkFormulas();
            if (!formulasToSolve) {
                while (!formulasToSolve) {
                    Thread.sleep(3600000);
                    formulasToSolve = checkFormulas();
                }
            }
        }
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
                if (formulas.size() != 0) {
                    return true;
                }
            }
            maxConnectives++;
        }
        return false;
    }

    /**
     * This method initializes and starts the Twitter publisher. It gets called once the first tautology was stored in
     * the tautology list.
     */
    private void startPublisher() {
        Thread thread = new Thread(new Publisher(this));
        thread.start();
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
