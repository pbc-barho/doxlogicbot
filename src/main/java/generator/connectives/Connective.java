package generator.connectives;

import generator.Formula.Formula;
import generator.Formula.TableauFormula;
import io.Serializer;
import lombok.Getter;
import lombok.Setter;
import solver.Branch;
import solver.Tableau;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The abstract super class implementing the methods for all connectives.
 */
@Getter
@Setter
public abstract class Connective implements Serializable {

    @Serial
    private static final long serialVersionUID = 12L;

    private int length;
    private final String symbol;
    private int modalDepth;

    /**
     * Constructor of a connective.
     * @param symbol The printed symbol for the connective.
     */
    public Connective(String symbol) {
        this.symbol = symbol;
    }

    /**
     * This method applies the rule of the corresponding connective.
     * @param tableau The tableau on which the formula with the connective occurs.
     * @param branch The branch to which the rule should be applied.
     * @param formula The formula that contains the connective.
     */
    public abstract void applyRule(Tableau tableau, Branch branch, TableauFormula formula);

    /**
     * This method applies the rule of the corresponding negated connective.
     * @param tableau The tableau on which the formula with the connective occurs.
     * @param branch The branch to which the rule should be applied.
     * @param formula The formula that contains the negated connective.
     */
    public abstract void applyNegatedRule(Tableau tableau, Branch branch, TableauFormula formula);

    /**
     * This method generates a maximum of 286 random formulas for each complexity (measured by the number of
     * connectives). These formulas are used to evaluate the performance of the tableau solver.
     * @param nrConnectives The number of connectives of the formulas that should be generated.
     * @return A list of up to 286 formulas that were generated.
     */
    public abstract ArrayList<Formula> generateSelectedFormulas(int nrConnectives);

    /**
     * This method generates all possible formulas by combining older (less complex) formulas with a connective.
     * @param nrConnectives The number of connectives of the generated formulas to store the formulas in the
     *                      corresponding file.
     * @return A list containing all newly generated formulas.
     */
    public abstract ArrayList<Formula> generateAllFormulas(int nrConnectives);

    /**
     * This method chooses a random formula out of a formula list.
     * @param formulas The formula list out of which one random formula should be retrieved.
     * @return The random formula.
     */
    public Formula chooseRandomFormula(ArrayList<Formula> formulas) {
        int randomIndex = (int)(Math.random() * formulas.size());
        return formulas.get(randomIndex);
    }

    /**
     * This method indicates whether a formula is duplicated.
     * @param newFormula The formula that gets checked on whether it is duplicated.
     * @param newFormulas The list of generated formulas.
     * @return A boolean variable indicating whether the formula is duplicated.
     */
    public boolean duplicatedFormula(Formula newFormula, ArrayList<Formula> newFormulas) {
        return newFormulas.stream().anyMatch(oldFormula -> oldFormula.getFormulaTree().equals(newFormula.getFormulaTree()));
    }
}
