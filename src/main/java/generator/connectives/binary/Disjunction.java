package generator.connectives.binary;

import generator.formula.Complexity;
import generator.formula.Formula;
import generator.formula.TableauFormula;
import generator.tree.ConnectiveNode;
import generator.tree.Node;
import generator.tree.Tree;
import generator.connectives.unary.Negation;
import solver.Branch;
import solver.Tableau;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class implements the methods for the following connective: Disjunction.
 */
public class Disjunction extends BinarySymmetricConnective implements Serializable {

    @Serial
    private static final long serialVersionUID = 6L;

    /**
     * The constructor of the disjunction including its printing symbol.
     */
    public Disjunction() {
        super("∨");
    }

    @Override
    public synchronized void applyRule(Tableau tableau, Branch branch, TableauFormula formula) {
        Node leftNode = formula.getFormulaTree().getRoot().getLeft();
        Node rightNode = formula.getFormulaTree().getRoot().getRight();

        Complexity childComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);

        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - this.length + 1, childComplexity, formula.getAgents());

        TableauFormula rightChild = new TableauFormula(new Tree(rightNode), formula.getState(),
                formula.getLength() - this.length + 1, childComplexity, formula.getAgents());

        Branch rightBranch = new Branch(branch);
        tableau.addBranch(rightBranch);

        rightBranch.addFormula(rightBranch.getFormulasOnBranch(), rightChild);
        branch.addFormula(branch.getFormulasOnBranch(), leftChild);

        if (leftNode instanceof ConnectiveNode) branch.addFormula(leftChild);
        if (rightNode instanceof ConnectiveNode) rightBranch.addFormula(rightChild);
    }

    @Override
    public void applyNegatedRule(Tableau tableau, Branch branch, TableauFormula formula) {
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

        branch.addFormula(branch.getNegatedFormulasOnBranch(), leftChild);
        branch.addFormula(branch.getNegatedFormulasOnBranch(), rightChild);

        if (leftNode instanceof ConnectiveNode) branch.addFormula(negatedLeftChild);
        if (rightNode instanceof ConnectiveNode) branch.addFormula(negatedRightChild);
    }
}
