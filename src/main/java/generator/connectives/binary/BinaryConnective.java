package generator.connectives.binary;

import generator.formula.Formula;
import generator.formula.TableauFormula;
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
 * This class implements the methods for binary connectives.
 */
@Getter
@Setter
public abstract class BinaryConnective extends Connective implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;

    /**
     * The length of the printed binary connective including white space.
     */
    public int length = 3;

    private final int modalDepth = 0;

    /**
     * Constructor of the binary connective.
     * @param symbol The printed symbol for the connective.
     */
    public BinaryConnective(String symbol) {
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
                for (Formula formula1 : firstFormulas) {
                    for (Formula formula2 : secondFormulas) {
                        newFormulas.add(new Formula(this, formula1, formula2));
                    }
                }
            }
        }
        return newFormulas;
    }
}
