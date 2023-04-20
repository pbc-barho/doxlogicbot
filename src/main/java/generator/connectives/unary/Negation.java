package generator.connectives.unary;

import generator.Formula.Complexity;
import generator.Formula.TableauFormula;
import generator.Tree.ConnectiveNode;
import generator.Tree.Node;
import generator.Tree.Tree;
import lombok.Getter;
import lombok.Setter;
import solver.Branch;
import solver.Tableau;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class implements the methods for the following connective: Negation.
 */
@Getter
@Setter
public class Negation extends UnaryConnective implements Serializable {

    @Serial
    private static final long serialVersionUID = 9L;

    /**
     * The length of the printed negation.
     */
    public int length = 1;
    private final int modalDepth = 0;

    /**
     * Constructor of the negation including its printing symbol.
     */
    public Negation() {
        super("¬");
    }

    @Override
    public void applyRule(Tableau tableau, Branch branch, TableauFormula formula) {
        Node leftNode = formula.getFormulaTree().getRoot().getLeft();
        Complexity leftChildComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);
        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - 1, leftChildComplexity, formula.getAgents());

        branch.addFormula(branch.getNegatedFormulasOnBranch(), leftChild);

        if (leftNode instanceof ConnectiveNode)
            ((ConnectiveNode) leftNode).getValue().applyNegatedRule(tableau, branch, leftChild);
    }

    @Override
    public void applyNegatedRule(Tableau tableau, Branch branch, TableauFormula formula) {
        Node leftNode = formula.getFormulaTree().getRoot().getLeft();
        Complexity leftChildComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);
        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - 1, leftChildComplexity, formula.getAgents());
        branch.addFormula(branch.getFormulasOnBranch(), leftChild);
        if (leftNode instanceof ConnectiveNode) branch.addFormula(leftChild);
    }
}
