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
 * This class implements the methods for the following connective: Biimplication.
 */
public class Biimplication extends BinarySymmetricConnective implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * The constructor of the biimplication including its printing symbol.
     */
    public Biimplication() {
        super("⇿");
    }

    @Override
    public synchronized void applyRule(Tableau tableau, Branch branch, TableauFormula formula) {
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

        branch.getFormulasOnBranch().add(leftChild);
        branch.getFormulasOnBranch().add(rightChild);
        rightBranch.getNegatedFormulasOnBranch().add(leftChild);
        rightBranch.getNegatedFormulasOnBranch().add(rightChild);

        if (leftNode instanceof ConnectiveNode) {
            branch.addFormula(leftChild);
            rightBranch.addFormula(negatedLeftChild);
        }
        if (rightNode instanceof ConnectiveNode) {
            branch.addFormula(rightChild);
            rightBranch.addFormula(negatedRightChild);
        }
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

        branch.addFormula(branch.getFormulasOnBranch(), leftChild);
        branch.addFormula(branch.getNegatedFormulasOnBranch(), rightChild);
        rightBranch.addFormula(rightBranch.getNegatedFormulasOnBranch(), leftChild);
        rightBranch.addFormula(rightBranch.getFormulasOnBranch(), rightChild);

        if (leftNode instanceof ConnectiveNode) {
            branch.addFormula(leftChild);
            rightBranch.addFormula(negatedLeftChild);
        }
        if (rightNode instanceof ConnectiveNode) {
            branch.addFormula(negatedRightChild);
            rightBranch.addFormula(rightChild);
        }
    }
}
