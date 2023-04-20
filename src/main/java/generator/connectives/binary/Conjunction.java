package generator.connectives.binary;

import generator.Formula.Complexity;
import generator.Formula.Formula;
import generator.Formula.TableauFormula;
import generator.Tree.ConnectiveNode;
import generator.Tree.Node;
import generator.Tree.Tree;
import generator.connectives.unary.Negation;
import solver.Branch;
import solver.Tableau;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class implements the methods for the following connective: Conjunction.
 */
public class Conjunction extends BinarySymmetricConnective implements Serializable {

    @Serial
    private static final long serialVersionUID = 5L;

    /**
     * The constructor of the conjunction including its printing symbol.
     */
    public Conjunction() {
        super("∧");
    }

    @Override
    public void applyRule(Tableau tableau, Branch branch, TableauFormula formula) {
        Node leftNode = formula.getFormulaTree().getRoot().getLeft();
        Node rightNode = formula.getFormulaTree().getRoot().getRight();

        Complexity childComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);

        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - this.length + 1, childComplexity, formula.getAgents());

        TableauFormula rightChild = new TableauFormula(new Tree(rightNode), formula.getState(),
                formula.getLength() - this.length + 1, childComplexity, formula.getAgents());

        branch.addFormula(branch.getFormulasOnBranch(), leftChild);
        branch.addFormula(branch.getFormulasOnBranch(), rightChild);

        if (leftNode instanceof ConnectiveNode) branch.addFormula(leftChild);
        if (rightNode instanceof ConnectiveNode) branch.addFormula(rightChild);
    }

    @Override
    public synchronized void applyNegatedRule(Tableau tableau, Branch branch, TableauFormula formula) {
        Node leftNode = formula.getFormulaTree().getRoot().getLeft();
        Node rightNode = formula.getFormulaTree().getRoot().getRight();

        Complexity childComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);

        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - this.length + 1, childComplexity, formula.getAgents());
        TableauFormula negatedLeftChild = new TableauFormula(new Formula(new Negation(), leftChild), formula.getState());

        TableauFormula rightChild = new TableauFormula(new Tree(rightNode), formula.getState(),
                formula.getLength() - this.length + 1, childComplexity, formula.getAgents());
        TableauFormula negatedRightChild = new TableauFormula(new Formula(new Negation(), rightChild), formula.getState());

        Branch rightBranch = new Branch(branch);
        tableau.addBranch(rightBranch);

        rightBranch.addFormula(rightBranch.getNegatedFormulasOnBranch(), rightChild);
        branch.addFormula(branch.getNegatedFormulasOnBranch(), leftChild);

        if (leftNode instanceof ConnectiveNode) branch.addFormula(negatedLeftChild);
        if (rightNode instanceof ConnectiveNode) rightBranch.addFormula(negatedRightChild);
    }
}
