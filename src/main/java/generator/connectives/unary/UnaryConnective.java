package generator.connectives.unary;

import generator.Formula.Formula;
import generator.Formula.TableauFormula;
import generator.connectives.Connective;
import io.Serializer;
import lombok.Getter;
import lombok.Setter;
import solver.Branch;
import solver.Tableau;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class implements the methods for unary connectives.
 */
@Getter
@Setter
public abstract class UnaryConnective extends Connective implements Serializable {

    @Serial
    private static final long serialVersionUID = 11L;

    /**
     * The default length of a printed unary connective.
     */
    public int length = 1;
    private int modalDepth;

    /**
     * Constructor of the unary connective.
     * @param symbol The printed symbol for the connective.
     */
    public UnaryConnective(String symbol) {
        super(symbol);
    }

    @Override
    public abstract void applyRule(Tableau tableau, Branch branch, TableauFormula formula);

    @Override
    public abstract void applyNegatedRule(Tableau tableau, Branch branch, TableauFormula formula);

    @Override
    public ArrayList<Formula> generateSelectedFormulas(int nrConnectives) {
        ArrayList<Formula> newFormulas = new ArrayList<>();
        ArrayList<Formula> formulas = Serializer.loadFormulas(nrConnectives + "_nr_connectives.ser");
        while (newFormulas.size() <= 25 && formulas != null && formulas.size() > 0) {
            int oldSize = newFormulas.size();
            Formula randomFormula = chooseRandomFormula(formulas);
            Formula newFormula = new Formula(this, randomFormula);
            while (duplicatedFormula(newFormula, newFormulas) && formulas.size() > 1) {
                formulas.remove(randomFormula);
                randomFormula = chooseRandomFormula(formulas);
                newFormula = new Formula(this, randomFormula);
            }
            formulas.remove(randomFormula);
            if (!duplicatedFormula(newFormula, newFormulas)) newFormulas.add(newFormula);
            if (newFormulas.size() == oldSize) break;
        }
        return newFormulas;
    }

    @Override
    public ArrayList<Formula> generateAllFormulas(int nrConnectives) {
        ArrayList<Formula> newFormulas = new ArrayList<>();
        ArrayList<Formula> formulas = Serializer.loadFormulas(nrConnectives + "_nr_connectives.ser");
        if (formulas != null) for (Formula formula : formulas) newFormulas.add(new Formula(this, formula));
        return newFormulas;
    }
}