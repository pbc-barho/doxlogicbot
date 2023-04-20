package generator.formula;

import generator.tree.Node;
import generator.tree.Tree;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class representing formulas that occur on a tableau.
 */
@Getter
@Setter
public class TableauFormula extends Formula implements Serializable {

    @Serial
    private static final long serialVersionUID = 17L;

    private int priority = 0;
    private int state;

    /**
     * Constructor for a tableau formula that starts a tableau.
     * @param formula The formula instance that starts the tableau in state 0.
     */
    public TableauFormula(Formula formula) {
        super(formula);
        this.state = 0;
        choosePriority();
    }

    /**
     * Constructor for a tableau formula that gets introduced on the tableau in a specific state.
     * @param formula The formula instance that get introduced.
     * @param state The state of the formula.
     */
    public TableauFormula(Formula formula, int state) {
        super(formula);
        this.state = state;
        choosePriority();
    }

    /**
     * The constructor of a tableau formula that gets introduced using a part of another formula.
     * @param formulaTree The formula tree of the new tableau formula.
     * @param state The state in which the tableau formula gets introduced.
     * @param length The length of the tableau formula.
     * @param complexity The complexity of the tableau formula.
     * @param agents The agents that appear in the tableau formula.
     */
    public TableauFormula(Tree formulaTree, int state, int length, Complexity complexity, ArrayList<Agent> agents) {
        super(formulaTree, length, complexity, agents);
        this.state = state;
        choosePriority();
    }

    /**
     * A method that chooses the priority of a formula based on the main connective appearing in the formula. If the
     * main connective is a negation, the priority gets determined based on the main connectives besides the negation.
     */
    public void choosePriority() {
        if (getLength() > 1) {
            Node root = getFormulaTree().getRoot();
            switch(root.toString().charAt(0)){
                case '¬' -> {
                    Node left = root.getLeft();
                    switch (left.toString().charAt(0)) {
                        case '¬' -> setPriority(1);
                        case '∨', '→' -> setPriority(2);
                        case 'M' -> setPriority(3);
                        case 'B' -> setPriority(4);
                        case '∧', '⇿' -> setPriority(5);
                        default -> setPriority(0);
                    }
                }
                case '∧' -> setPriority(2);
                case 'B' -> setPriority(3);
                case 'M' -> setPriority(4);
                case '∨', '→', '⇿' -> setPriority(5);
                default -> setPriority(0);
            }
        }
    }

    /**
     * A method that compares two tableau formulas.
     * @param formula The tableau formula that the formula gets compared to.
     * @return A boolean variable indicating whether the tableau formulas have the same formula tree and are in the
     * same state.
     */
    public boolean isEqual(TableauFormula formula){
        return getFormulaTree().equals(formula.getFormulaTree()) && state == formula.getState();
    }
}
