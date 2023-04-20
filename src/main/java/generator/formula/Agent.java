package generator.Formula;

import lombok.Getter;
import lombok.Setter;
import solver.Branch;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class implements all methods concerning an agent.
 */
@Getter
@Setter
public class Agent implements Serializable {

    @Serial
    private static final long serialVersionUID = 13L;

    private final String symbol;
    private ArrayList<Pair> relations = new ArrayList<>();

    /**
     * Constructor of the agent.
     * @param symbol The natural number representing the agent.
     */
    public Agent(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Checks if a relation between two states exist for that agent and adds the relation if it does not.
     * @param world1 The first state of the relation.
     * @param world2 The state that the first state relates to.
     */
    public void addRelation(int world1, int world2){
        boolean add = true;
        for (Pair relation : relations)
            if (relation.getValue0() == world1 && relation.getValue1() == world2) {
                add = false;
                break;
            }
        if(add) relations.add(new Pair(world1, world2));
    }

    /**
     * Removes all relations of an agent when a new formula gets solved (leading to a new tableau).
     */
    public void reset(){
        relations = new ArrayList<>();
    }

    /**
     * A method that checks whether a state relates to any other state.
     * @param world The state for which the relation gets checked.
     * @return Boolean variable indicating whether a relation exists.
     */
    private boolean worldRelationExists(int world){
        return relations.stream().anyMatch(pair -> pair.getValue0().equals(world));
    }

    /**
     * Applying the relational constraints (transitivity, euclideanicity, seriality) to the list of relations of the
     * agent.
     * @param worlds The list of states that occur on the branch.
     * @param branch The branch on which the relational constraints exist.
     */
    public void checkRelations(ArrayList<Integer> worlds, Branch branch) {
        ArrayList<Pair> relationsOld;
        do {
            relationsOld = new ArrayList<>(relations);
            ArrayList<Pair> copyRelations1 = new ArrayList<>(relations);
            ArrayList<Pair> copyRelations2 = new ArrayList<>(relations);
            copyRelations1.forEach(relation1 -> {
                copyRelations2.forEach(relation2 -> {
                    // transitivity
                    if (relation1.getValue1().equals(relation2.getValue0()))
                        addRelation(relation1.getValue0(), relation2.getValue1());
                    //  euclideanicity
                    if (relation1.getValue0().equals(relation2.getValue0()))
                        addRelation(relation1.getValue1(), relation2.getValue1());
                });
            });
            // seriality
            ArrayList<Integer> copyWorlds = new ArrayList<>(worlds);
            copyWorlds.forEach(world -> {
                if (!worldRelationExists(world)){
                    int newState = Collections.max(worlds) + 1;
                    addRelation(world, newState);
                    worlds.add(newState);
                }
            });
        } while (relationsOld.size() != relations.size());
        worlds.forEach(world -> {
            if (!branch.getWorlds().contains(world)) branch.getWorlds().add(world);
        });
    }
}
