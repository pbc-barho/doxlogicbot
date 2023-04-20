package generator.Formula;

import generator.Tree.*;
import generator.connectives.unary.Belief;
import generator.connectives.Connective;
import generator.connectives.unary.Possibility;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a formula.
 */
@Getter
@Setter
public class Formula implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int MAX_LENGTH = 280;

    private Complexity complexity = new Complexity(0, 0);
    private int length;
    private Tree formulaTree;
    private ArrayList<Agent> agents = new ArrayList<>();

    /**
     * The status of the formula: 0 = it is not solved yet, 1 = it is not a tautology, 2 = it is a tautology.
     */
    private int status = 0;

    /**
     * The constructor of a formula that only consists of a propositional atom.
     * @param atom The propositional atom that the formula consists of.
     */
    public Formula(PropAtom atom) {
        AtomNode atomNode = new AtomNode(atom);
        this.formulaTree = new Tree(atomNode);
        setComplexity(null, null, 0, 0);
        this.length = 1;
    }

    /**
     * The constructor that copies another given formula.
     * @param formula The formula that gets copied.
     */
    public Formula(Formula formula) {
        this.formulaTree = formula.getFormulaTree();
        this.length = formula.getLength();
        this.complexity = formula.getComplexity();
        this.status = formula.getStatus();
        this.agents = formula.getAgents();
    }

    /**
     * The constructor of a formula that appears on a tableau and does not get evaluated.
     * @param formulaTree The formula tree of the formula.
     * @param length The length of the formula.
     * @param complexity The complexity of the formula.
     * @param agents The agents that appear in the formula.
     */
    public Formula(Tree formulaTree, int length, Complexity complexity, ArrayList<Agent> agents) {
        this.formulaTree = formulaTree;
        this.length = length;
        this.complexity = complexity;
        this.agents = agents;
    }

    /**
     * The constructor for a formula that gets made by adding a unary connective to a given formula.
     * @param connective The unary connective that gets added to a formula.
     * @param currentFormula The formula that the connective gets added to.
     */
    public Formula(Connective connective, Formula currentFormula) {
        ConnectiveNode node = new ConnectiveNode(connective);
        node.setChildren(currentFormula.getFormulaTree().getRoot(), null);
        formulaTree = new Tree(node);
        setComplexity(currentFormula.getComplexity(), null, connective.getModalDepth(), 1);
        length = currentFormula.getLength() + connective.getLength();
        if (connective instanceof Belief) {
            addAgent(((Belief) connective).getAgent());
        } else if (connective instanceof Possibility){
            addAgent(((Possibility) connective).getAgent());
        }
        currentFormula.getAgents().forEach(this::addAgent);
    }

    /**
     * The constructor for a formula that gets made by combining two formulas with a binary connective.
     * @param connective The binary connectives that combines the two formulas.
     * @param formula1 The first formula that gets used for the new formula.
     * @param formula2 The second formula that gets used for the new formula.
     */
    public Formula(Connective connective, Formula formula1, Formula formula2) {
        ConnectiveNode node = new ConnectiveNode(connective);
        node.setChildren(formula1.getFormulaTree().getRoot(), formula2.getFormulaTree().getRoot());
        formulaTree = new Tree(node);
        setComplexity(formula1.getComplexity(), formula2.getComplexity(), 0, 1);
        length = formula1.getLength() + formula2.getLength() + connective.getLength() + 2;
        formula1.getAgents().forEach(this::addAgent);
        formula2.getAgents().forEach(this::addAgent);
    }

    /**
     * A method that adds an agent to the agent-list of the formula to keep up with the agents that occur in the formula.
     * @param agent The agents that gets added if it is not part of the list yet.
     */
    private void addAgent(Agent agent){
        if (!agents.contains(agent)){
            agents.add(agent);
        }
    }

    /**
     * A method that changes the complexity of the formula based on the connective and formula(s) that it was build with.
     * @param oldComplexity1 The complexity of one formula that was used to build the new formula.
     * @param oldComplexity2 The complexity of the second formula that was used to build the new formula.
     * @param newModalDepth The increase of the modal depth based on the connective that was used to build the new
     *                      formula with.
     * @param newNrConnectives The increase of the number of connectives based on whether a connective was used to build
     *                         the formula.
     */
    public void setComplexity(Complexity oldComplexity1, Complexity oldComplexity2, int newModalDepth, int newNrConnectives) {
        if (oldComplexity1 == null) {
            complexity.setModalDepth(newModalDepth);
            complexity.setNrConnectives(newNrConnectives);
        } else if (oldComplexity2 == null) {
            complexity.setModalDepth(oldComplexity1.getModalDepth() + newModalDepth);
            complexity.setNrConnectives(oldComplexity1.getNrConnectives() + newNrConnectives);
        } else {
            complexity.setModalDepth(oldComplexity1.getModalDepth() + oldComplexity2.getModalDepth() + newModalDepth);
            complexity.setNrConnectives(oldComplexity1.getNrConnectives() + oldComplexity2.getNrConnectives() + newNrConnectives);
        }
    }

    /**
     * A method that accounts for the outer brackets of the formula (that are not printed).
     * @return The length of the formula.
     */
    public int getFinalLength(){
        return length - 2;
    }

    /**
     * A method that checks whether a formula is too long to be printed on Twitter.
     * @return A boolean variable that indicates whether the formula is too long or not.
     */
    public boolean isFinal(){
        return getFinalLength() >= MAX_LENGTH;
    }

}
