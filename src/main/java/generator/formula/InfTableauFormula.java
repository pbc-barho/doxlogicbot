package generator.formula;

import generator.tree.Node;
import generator.connectives.Connective;
import solver.Branch;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that represents formulas that can be re-applied on the branch using new relations.
 */
public class InfTableauFormula extends TableauFormula implements Serializable {

    @Serial
    private static final long serialVersionUID = 16L;

    private ArrayList<Pair> oldRelations;

    /**
     * Constructor for the formulas that can be re-applied.
     * @param formula The original instance of a formula that can be re-applied.
     * @param relations The relations that already exist on the branch of the tableau on which the formula exists.
     */
    public InfTableauFormula(Formula formula, ArrayList<Pair> relations) {
        super(formula);
        this.oldRelations = new ArrayList<>(relations);
    }

    /**
     * If a new relation gets added to the branch on which the formula is places, this method checks whether the formula
     * can be re-applied with that new relation. If it can be re-applied, the rule of the formulas main connective is
     * applied.
     * @param relations All relations of the agent that is occurring in the formula.
     * @param branch The branch on which the formula should be re-applied.
     */
    public void applyNewRelations(ArrayList<Pair> relations, Branch branch) {
        relations.forEach(relation1 -> {
            if (relation1.getValue0().equals(this.getState()))
                if (!oldRelations.contains(new Pair(this.getState(), relation1.getValue1()))) {
                    Node root = this.getFormulaTree().getRoot();
                    if (!root.isLeaf()) ((Connective) root.getValue()).applyRule(branch.getTableau(), branch, this);
                }
        });
    }
}
