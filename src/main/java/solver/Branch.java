package solver;

import generator.formula.Agent;
import generator.formula.Formula;
import generator.formula.InfTableauFormula;
import generator.formula.TableauFormula;
import generator.tree.Node;
import generator.connectives.unary.Belief;
import generator.connectives.Connective;
import generator.connectives.unary.Negation;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A class that implements a branch on a tableau.
 */
@Getter
@Setter
public class Branch {

    private final int STOPTIME = 10000;

    /**
     * An inner class that implements a comparator for comparing the priority of formulas. This comparator is used
     * for a priority queue that is used to store formulas based on their priorities.
     */
    private static class FormulaComparator implements Comparator<TableauFormula> {

        /**
         * A method that compares the priorities of two formulas.
         * @param o1 The first tableau formula.
         * @param o2 The second tableau formula.
         * @return An integer indicating how the priorities of the two formulas compare.
         */
        @Override
        public int compare(TableauFormula o1, TableauFormula o2) {
            return Integer.compare(o1.getPriority(), o2.getPriority());
        }
    }

    private PriorityQueue<TableauFormula> leftOverFormulas = new PriorityQueue<>(new Branch.FormulaComparator());

    private ArrayList<TableauFormula> formulasOnBranch = new ArrayList<>();
    private ArrayList<TableauFormula> negatedFormulasOnBranch = new ArrayList<>();
    private ArrayList<InfTableauFormula> infFormulas = new ArrayList<>();
    private ArrayList<Agent> agents;
    private ArrayList<Integer> worlds = new ArrayList<>();

    /**
     * The status of a branch: 0 = it is not solved yet, 1 = it is open & complete, 2 = it is closed.
     */
    private int branchStatus = 0;

    private TableauFormula currentFormula;
    private Tableau tableau;

    /**
     * Constructor of a branch on a tableau.
     * @param tableau The tableau that the branch will be on.
     * @param currentFormula The formula that gets currently solved.
     */
    public Branch(Tableau tableau, Formula currentFormula) {
        this.tableau = tableau;
        leftOverFormulas.add(new TableauFormula(currentFormula));
        worlds.add(currentFormula.getStatus());
        agents = new ArrayList<>(currentFormula.getAgents());
    }

    /**
     * Constructor that copies another branch.
     * @param copyBranch The branch that gets copied.
     */
    public Branch(Branch copyBranch) {
        this.tableau = copyBranch.getTableau();
        this.leftOverFormulas = new PriorityQueue<>(copyBranch.getLeftOverFormulas());
        this.formulasOnBranch = new ArrayList<>(copyBranch.getFormulasOnBranch());
        this.negatedFormulasOnBranch = new ArrayList<>(copyBranch.getNegatedFormulasOnBranch());
        this.agents = new ArrayList<>(copyBranch.getAgents());
        this.worlds = new ArrayList<>(copyBranch.getWorlds());
        this.currentFormula = null;
        this.infFormulas = new ArrayList<>(infFormulas);
    }

    /**
     * A method that adds a formula to a branch.
     * @param formulaList The list of formulas on the current branch.
     * @param formula The formula that should be added to the branch.
     */
    public void addFormula(ArrayList<TableauFormula> formulaList, TableauFormula formula) {
        if (!containsFormula(formulaList, formula)) formulaList.add(formula);
    }

    /**
     * A method that indicates whether a formula is already on the branch.
     * @param formulaList The list of formulas on the current branch.
     * @param formula The formula that is checked on whether it is already on the branch.
     * @return A boolean variable indicating whether the formula is already on the branch.
     */
    public boolean containsFormula(ArrayList<TableauFormula> formulaList, TableauFormula formula){
        for (TableauFormula tableauFormula : formulaList) if (tableauFormula.isEqual(formula)) return true;
        return false;
    }

    /**
     * A method that adds a formula to the branch.
     * @param formula The formula that should be added to the branch.
     */
    public void addFormula(TableauFormula formula) {
        leftOverFormulas.add(formula);
    }

    /**
     * A method that implements the solving of a branch. Whilst there are still formulas on the branch that should be
     * solved it always applies the rule of the main connective to the formula with the highest complexity (on the
     * branch). If the branch is not solved within 10 seconds it times out.
     * @return A boolean variable indicating whether the branch closed or not. A timed out branch is treated as not
     * being closed.
     */
    public boolean solveBranch(){
        long startTime = System.currentTimeMillis();
        while (!leftOverFormulas.isEmpty() && (System.currentTimeMillis()-startTime)<STOPTIME){
            currentFormula = leftOverFormulas.poll();
            assert currentFormula != null;
            Node root = currentFormula.getFormulaTree().getRoot();
            if (!root.isLeaf()) {
                ((Connective) root.getValue()).applyRule(tableau, this, currentFormula); //currentFormula.getFormulaTree());
            } else {
                formulasOnBranch.add(currentFormula);
            }
            if (isCLosed()) return true;
            if ((System.currentTimeMillis()-startTime)>=STOPTIME) break;
            checkRelationsPerAgent();
            if ((System.currentTimeMillis()-startTime)>=STOPTIME) break;
            ArrayList<InfTableauFormula> copyInfFormulas = new ArrayList<>(infFormulas);
            copyInfFormulas.forEach(infFormula -> infFormula.applyNewRelations(((Belief)
                    infFormula.getFormulaTree().getRoot().getValue()).getAgent().getRelations(), this));
        }
        if (isCLosed()) {
            return true;
        } else if (System.currentTimeMillis()-startTime >= STOPTIME) {
            return false;
        }
        return !leftOverFormulas.isEmpty();
    }

    /**
     * A method that checks the relations for each agent that occurs on the branch once a new relation gets added.
     */
    private void checkRelationsPerAgent() {
        agents.forEach(agent -> agent.checkRelations(worlds, this));
    }

    /**
     * A method that indicates whether a branch is closed. It loops through the formulas and the negated
     * formulas on the branch to check whether two are equal.
     * @return A boolean variable indicating whether the branch is closed.
     */
    private boolean isCLosed() {
        ArrayList<TableauFormula> copyNegatedFormulas = new ArrayList<>(negatedFormulasOnBranch);
        for (TableauFormula formula : copyNegatedFormulas) {
            for (TableauFormula formula2 : formulasOnBranch) {
                if (formula.isEqual(formula2)) return true;
            }
            for (TableauFormula formula2 : copyNegatedFormulas) {
                TableauFormula negatedFormula = new TableauFormula(new Formula (new Negation(), formula2), formula2.getState());
                if (formula.isEqual(negatedFormula)) return true;
            }
        }
        return false;
    }
}
