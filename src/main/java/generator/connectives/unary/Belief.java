package generator.connectives.unary;

import generator.formula.Agent;
import generator.formula.Complexity;
import generator.formula.Formula;
import generator.formula.InfTableauFormula;
import generator.formula.TableauFormula;
import generator.tree.AtomNode;
import generator.tree.ConnectiveNode;
import generator.tree.Node;
import generator.tree.Tree;
import lombok.Getter;
import lombok.Setter;
import solver.Branch;
import solver.Tableau;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class implements the methods for the belief operator.
 */
@Getter
@Setter
public class Belief extends UnaryConnective implements Serializable {

    @Serial
    private static final long serialVersionUID = 8L;

    private final Agent agent;
    private int length = 2;
    private final int modalDepth = 1;

    /**
     * Constructor of the belief operator.
     * @param agent The corresponding agent of the belief operator.
     */
    public Belief(Agent agent) {
        super("B" + agent.getSymbol());
        this.agent = agent;
    }

    /**
     * This method checks whether the list of formulas that can be re-applied already contains a specific formula.
     * @param formulaList The list of formulas that can be re-applied.
     * @param formula The formula that gets checked whether it is part of the formula list.
     * @return Boolean variable indicating whether the formula is already part of the list.
     */
    public boolean containsFormula(ArrayList<InfTableauFormula> formulaList, TableauFormula formula){
        for (TableauFormula tableauFormula : formulaList) {
            if (tableauFormula.isEqual(formula)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void applyRule(Tableau tableau, Branch branch, TableauFormula formula) {

        if (!containsFormula(branch.getInfFormulas(), new InfTableauFormula(formula, agent.getRelations())))
            branch.getInfFormulas().add(new InfTableauFormula(formula, agent.getRelations()));

        Node leftNode = formula.getFormulaTree().getRoot().getLeft();

        Complexity leftChildComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);
        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - 2, leftChildComplexity, formula.getAgents());

        agent.getRelations().forEach(pair -> {
            if (pair.getValue0().equals(formula.getState())){
                leftChild.setState(pair.getValue1());
                branch.addFormula(branch.getFormulasOnBranch(), leftChild);
                if (leftNode instanceof ConnectiveNode) {
                    if (!(leftNode.getValue() instanceof Negation) || !(leftNode.getLeft() instanceof AtomNode)){
                        branch.addFormula(leftChild);
                    }
                }
            }
        });
    }

    @Override
    public void applyNegatedRule(Tableau tableau, Branch branch, TableauFormula formula) {

        Node leftNode = formula.getFormulaTree().getRoot().getLeft();

        Complexity leftChildComplexity = new Complexity(formula.getComplexity().getModalDepth() - 1,
                formula.getComplexity().getNrConnectives() - 1);
        TableauFormula leftChild = new TableauFormula(new Tree(leftNode), formula.getState(),
                formula.getLength() - 2, leftChildComplexity, formula.getAgents());

        TableauFormula newFormula = new TableauFormula(new Formula(new Possibility(agent),
                new Formula(new Negation(), leftChild)), formula.getState());
        newFormula.setComplexity(formula.getComplexity());

        branch.addFormula(branch.getFormulasOnBranch(), newFormula);
        branch.addFormula(newFormula);
    }
}
