package generator.tree;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * A class that implements a tree that is used to represent a formula.
 */
@Setter
@Getter
public class Tree implements Serializable {

    @Serial
    private static final long serialVersionUID = 21L;

    private Node root;

    /**
     * Constructor for a tree.
     * @param root The root of the tree.
     */
    public Tree(Node root) {
        this.setRoot(root);
    }

    /**
     * Getter for the string that represents the root.
     * @return A string that represents the root.
     */
    public String getString(){
        return root.toString();
    }

    /**
     * A method that compares a tree with another tree.
     * @param otherTree The tree that this tree gets compared to.
     * @return A boolean variable indicating whether the trees are consisting of equal roots.
     */
    public boolean equals(Tree otherTree){
        return Objects.equals(getString(), otherTree.getString());
    }
}