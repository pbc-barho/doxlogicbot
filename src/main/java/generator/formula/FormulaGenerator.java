package generator.formula;

import generator.connectives.*;
import generator.connectives.binary.Biimplication;
import generator.connectives.binary.Conjunction;
import generator.connectives.binary.Disjunction;
import generator.connectives.binary.Implication;
import generator.connectives.unary.Belief;
import generator.connectives.unary.Negation;
import generator.connectives.unary.Possibility;
import io.Serializer;
import lombok.Getter;
import solver.TableauSolver;

import java.util.ArrayList;

/**
 * The class that implements the formula generator.
 */
@Getter
public class FormulaGenerator implements Runnable {

    private ArrayList<Formula> generatedFormulas = new ArrayList<>();
    private ArrayList<Connective> connectives = new ArrayList<>();
    private ArrayList<Agent> usedAgents = new ArrayList<>();

    private int nrConnectives;
    private int randomFile;

    /**
     * The empty constructor of the formula generator.
     */
    public FormulaGenerator() {
    }

    /**
     * The main method of the formula generator that generates all formulas and starts the tableau solver after
     * generating the formulas with one connective.
     */
    @Override
    public void run() {

        nrConnectives = 0;
        initializeConnectives();
        addAtoms();
        boolean running = true;
        storeFormulas();
        while (running) {
            generatedFormulas = new ArrayList<>();
            nrConnectives++;
            addFormulas();
            storeFormulas();

            if (nrConnectives == 1) new Thread(new TableauSolver(this)).start();

            if (generatedFormulas.isEmpty()) running = false;
        }
    }

    /**
     * This method chooses a random formula that has not been solved yet out of all existing files containing formulas
     * and deletes the formula afterwards from the file.
     * @return A random formula that has not been solved yet.
     */
    public Formula chooseRandomFormula() {

        randomFile = (int)(Math.random() * nrConnectives);
        if (randomFile == nrConnectives) randomFile -= 1;
        ArrayList<Formula> formulas = Serializer.loadFormulas(randomFile + "_nr_connectives_solve.ser");
        if (formulas == null || formulas.size() == 0) {
            ArrayList<Integer> existingFiles = new ArrayList<>();
            for (int i = 0; i < nrConnectives; i++){
                existingFiles.add(i);
            }
            existingFiles.remove(randomFile);
            formulas = chooseOtherFile(existingFiles);
        }
        if (formulas != null) {
            int randomIndex = (int)(Math.random() * formulas.size());
            Formula randomFormula = formulas.get(randomIndex);
            if (randomFormula.getStatus() != 0){
                return chooseRandomFormula();
            } else {
                formulas.remove(randomIndex);
                Serializer.saveFormulas(formulas, randomFile + "_nr_connectives_solve.ser");
                return randomFormula;
            }
        } else {
            return null;
        }
    }

    /**
     * This method is recursively loading files to find a random formula in one of the files that has not been solved
     * yet. If no other files are left it returns null.
     * @param existingFiles A list of generated files.
     * @return A list of formulas that were loaded from a file. It returns null if no such file exists.
     */
    private ArrayList<Formula> chooseOtherFile(ArrayList<Integer> existingFiles) {
        if (existingFiles.isEmpty()) {
            return null;
        }
        int newRandomIndex = (int)(Math.random() * existingFiles.size());
        randomFile = existingFiles.get(newRandomIndex);
        ArrayList<Formula> formulas = Serializer.loadFormulas(existingFiles.get(newRandomIndex) + "_nr_connectives_solve.ser");
        if (formulas == null || formulas.size() == 0) {
            existingFiles.remove(newRandomIndex);
            return chooseOtherFile(existingFiles);
        } else {
            return formulas;
        }
    }

    /**
     * A method that saves the three propositional atoms as formulas to a file.
     */
    private void addAtoms() {
        saveFormula(new Formula(PropAtom.P));
        saveFormula(new Formula(PropAtom.Q));
        saveFormula(new Formula(PropAtom.R));
    }

    /**
     * This method initiates the generation of all possible new formulas by reusing old formulas and each connective.
     */
    private void addFormulas() {
        connectives.forEach(connective -> {
            ArrayList<Formula> newFormulas;
            newFormulas = connective.generateAllFormulas(nrConnectives - 1);
            newFormulas.forEach(this::saveFormula);
        });
    }

    /**
     * A method that initializes an instance of each connective. The epistemic operators get initialized once for each
     * agent.
     */
    private void initializeConnectives() {
        connectives.add(new Negation());
        connectives.add(new Conjunction());
        connectives.add(new Disjunction());
        connectives.add(new Implication());
        connectives.add(new Biimplication());
        connectives.add(new Possibility(new Agent("\u2081")));
        connectives.add(new Possibility(new Agent("\u2082")));
        connectives.add(new Possibility(new Agent("\u2083")));
        connectives.add(new Belief(new Agent("\u2081")));
        connectives.add(new Belief(new Agent("\u2082")));
        connectives.add(new Belief(new Agent("\u2083")));
    }

    /**
     * A method that checks whether a formula adheres to the maximum character limit and whether it is not duplicated.
     * If that is the case it adds the formula to the list of newly generated formulas.
     * @param formula The formula that gets checked.
     */
    private void saveFormula(Formula formula) {
        if (!formula.isFinal() && !duplicatedFormula(formula)){
            generatedFormulas.add(formula);
        }
    }

    /**
     * A method that stores the newly generated formulas in two times two files. For each measure of the complexity
     * (number of connectives and modal depth) there are two different files. One file for solving the formulas and one
     * file for reusing the formulas for generating more complex ones.
     */
    private void storeFormulas() {
        if (!generatedFormulas.isEmpty()) {
            String fileName = nrConnectives + "_nr_connectives.ser";
            Serializer.saveFormulas(generatedFormulas, fileName);
            fileName = nrConnectives + "_nr_connectives_solve.ser";
            Serializer.saveFormulas(generatedFormulas, fileName); // for the solver to solve

            for (Formula generatedFormula : generatedFormulas) {
                fileName = generatedFormula.getComplexity().getModalDepth() + "_modal_depth.ser";
                Serializer.saveFormulasSingle(generatedFormula, fileName);
                fileName = generatedFormula.getComplexity().getModalDepth() + "_modal_depth_solve.ser";
                Serializer.saveFormulasSingle(generatedFormula, fileName);
            }
        }
    }

    /**
     * A method that indicates whether a formula was already generated once before.
     * @param formula The formula that gets checked on whether it is duplicated
     * @return A boolean variable indicating whether the formula is duplicated or not
     */
    private boolean duplicatedFormula(Formula formula) {
        return generatedFormulas.stream().anyMatch(oldFormula -> oldFormula.getFormulaTree().equals(formula.getFormulaTree()));
    }
}
