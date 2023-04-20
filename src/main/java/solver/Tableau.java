package solver;

import generator.Formula.Formula;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * A class that implements a tableau.
 */
@Setter @Getter
public class Tableau {

    private final Formula originalFormula;

    private ArrayList<Branch> branches = new ArrayList<>();

    private boolean tautology = true;

    /**
     * Constructor of a tableau. It starts with the negation of the formula that should be solved and starts the solving
     * method.
     * @param formula The negated formula that should be solved.
     */
    public Tableau(Formula formula) {
        originalFormula = formula;
        solve();
    }

    /**
     * A method that solves a tableau. It solves the tableau depth-first. If a branch is closed it gets deleted from the
     * list of branches and the next branch gets solved. If a branch is not closed (indicating that it either timed out
     * or open & complete) the original formula gets evaluated as being no tautology. If all branches are solved it
     * means that they all closed and the formula is a tautology.
     */
    private void solve(){
        branches.add(new Branch(this, originalFormula));
        while(originalFormula.getStatus() == 0) {
            Branch currentBranch = branches.get(0);
            if (currentBranch.solveBranch()) {
                branches.remove(currentBranch);
            } else {
                originalFormula.setStatus(1);
            }
            if (branches.isEmpty()) originalFormula.setStatus(2);
        }
    }

    /**
     * A method that adds a branch to the tableau.
     * @param branch The branch that gets added to the tableau.
     */
    public void addBranch(Branch branch) { // DFS
        branches.add(branch);
    }
}
