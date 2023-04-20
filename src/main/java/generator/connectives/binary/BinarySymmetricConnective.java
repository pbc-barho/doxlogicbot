package generator.connectives.binary;

import generator.formula.Formula;
import generator.formula.TableauFormula;
import io.Serializer;
import solver.Branch;
import solver.Tableau;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class implements the methods for binary symmetric connectives. The generation of formulas with these connectives
 * is restricted to avoid logically equivalent formulas.
 */
public abstract class BinarySymmetricConnective extends BinaryConnective implements Serializable {

    @Serial
    private static final long serialVersionUID = 4L;

    /**
     * Constructor of the binary symmetric connective.
     * @param symbol The printed symbol for the connective.
     */
    public BinarySymmetricConnective(String symbol) {
        super(symbol);
    }

    @Override
    public abstract void applyRule(Tableau tableau, Branch branch, TableauFormula formula);

    @Override
    public abstract void applyNegatedRule(Tableau tableau, Branch branch, TableauFormula formula);

    @Override
    public ArrayList<Formula> generateAllFormulas(int nrConnectives) {
        ArrayList<Formula> newFormulas = new ArrayList<>();
        for (int i = 0; i <= nrConnectives; i++) {
            ArrayList<Formula> firstFormulas = Serializer.loadFormulas(i + "_nr_connectives.ser");
            ArrayList<Formula> secondFormulas = Serializer.loadFormulas((nrConnectives - i) + "_nr_connectives.ser");
            if (firstFormulas != null && secondFormulas != null) {
                for (int j = 0; j < firstFormulas.size(); j++){
                    for (int k = j; k < secondFormulas.size(); k++){
                        newFormulas.add(new Formula(this, firstFormulas.get(j), secondFormulas.get(k)));
                    }
                }
            }
        }
        return newFormulas;
    }
}
