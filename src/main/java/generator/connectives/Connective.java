package generator.connectives;

import generator.formula.Formula;
import generator.formula.TableauFormula;
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
     * This method generates all possible formulas by combining older (less complex) formulas with a connective.
     * @param nrConnectives The number of connectives of the generated formulas to store the formulas in the
     *                      corresponding file.
     * @return A list containing all newly generated formulas.
     */
    public abstract ArrayList<Formula> generateAllFormulas(int nrConnectives);

}
