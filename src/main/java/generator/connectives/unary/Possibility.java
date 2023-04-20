package generator.connectives.unary;

import generator.formula.Agent;
import generator.formula.Complexity;
import generator.formula.Formula;
import generator.formula.TableauFormula;
import generator.tree.ConnectiveNode;
import generator.tree.Node;
import generator.tree.Tree;
import lombok.Getter;
import lombok.Setter;
import solver.Branch;
import solver.Tableau;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;

/**
 * This class implements the methods for the possibility operator.
 */
@Getter
@Setter
public class Possibility extends UnaryConnective implements Serializable {

    @Serial
    private static final long serialVersionUID = 10L;

    private final Agent agent;
    private int length = 2;
    private final int modalDepth = 1;

    /**
     * Constructor of the possibility operator.
     * @param agent The corresponding agent of the possibility operator.
     */
    public Possibility(Agent agent) {
        super("M" + agent.getSymbol());
        this.agent = agent;
    }

    @Override
    public void applyRule(Tableau tableau, Branch branch, TableauFormula formula) {
        Node leftNode = formula.getFormulaTree().getRoot().getLeft();

        Complexity leftChildComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);
        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - 2, leftChildComplexity, formula.getAgents());

        int newState = Collections.max(branch.getWorlds()) + 1;
        agent.addRelation(formula.getState(), newState);
        branch.getWorlds().add(newState);

        leftChild.setState(newState);
        branch.addFormula(branch.getFormulasOnBranch(), leftChild);
        if (leftNode instanceof ConnectiveNode) branch.addFormula(leftChild);
    }

    @Override
    public void applyNegatedRule(Tableau tableau, Branch branch, TableauFormula formula) {
        Node leftNode = formula.getFormulaTree().getRoot().getLeft();

        Complexity leftChildComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);
        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - 2, leftChildComplexity, formula.getAgents());

        TableauFormula newFormula = new TableauFormula(new Formula(new Belief(agent),
                new Formula(new Negation(), leftChild)), formula.getState());
        newFormula.setComplexity(formula.getComplexity());

        branch.addFormula(branch.getFormulasOnBranch(), newFormula);
        branch.addFormula(newFormula);
    }
}
