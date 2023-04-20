package generator.tree;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * An abstract class that represents a node of a tree.
 */
@Setter
@Getter
public abstract class Node implements Serializable {

    @Serial
    private static final long serialVersionUID = 20L;

    /**
     * The left and right children of the node.
     */
    protected Node left = null;
    protected Node right = null;

    /**
     * Empty constructor for a node.
     */
    public Node() {}

    /**
     * A setter for both children of the node.
     * @param newLeft The node that will be the new left child of the node.
     * @param newRight The node that will be the new right child of the node.
     */
    public void setChildren(Node newLeft, Node newRight) {
        left = newLeft;
        right = newRight;
    }

    /**
     * An abstract method that returns the value of a node.
     * @return The string that is used to print the main value of the node.
     */
    public abstract String printValue();

    /**
     * A method that is used to print a node by calling the recursive printing method and casting the string builder
     * to a string that can be printed.
     * @return A string that represents the node.
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        printString(buffer, "", "", true);
        return buffer.toString();
    }

    /**
     * Getter for the value of the node.
     * @return The object that is at the root of the node.
     */
    public abstract Object getValue();

    /**
     * A recursive method that prints the node and its nested children.
     * @param buffer A string builder that is used to make a sequence of the strings of the parts of the node.
     * @param prefix A prefix that is added before printing the value of a node.
     * @param childrenPrefix A prefix that is added before printing the children of a node.
     * @param first A boolean variable indicating whether this method gets called for the first time.
     */
    private void printString(StringBuilder buffer, String prefix, String childrenPrefix, boolean first) {
        boolean alreadyPrinted = false;
        boolean unary = false;
        if (!first) buffer.append("(");
        buffer.append(prefix);
        if (left != null ) if (left.isLeaf()) {
            if (right == null) {
                if (!first) buffer.deleteCharAt(buffer.length() - 1);
                alreadyPrinted = true;
                buffer.append(printValue()).append(left.printValue());
                unary = true;
            } else {
                buffer.append(left.printValue());
                if (!first) {
                    alreadyPrinted = true;
                    buffer.append(" ").append(printValue());
                }
            }
        } else {
            if (right == null) {
                if (!first) buffer.deleteCharAt(buffer.length() - 1);
                alreadyPrinted = true;
                buffer.append(printValue());
                unary = true;
            }
            left.printString(buffer, childrenPrefix, childrenPrefix, false);
        }
        if (first && !unary) {
            if (!(left == null && right == null)) buffer.append(" ");
            alreadyPrinted = true;
            buffer.append(printValue());
        }
        if (!alreadyPrinted) buffer.append(" ").append(printValue());
        if (right != null ) if (right.isLeaf()) {
            buffer.append(" ").append(right.printValue());
        } else {
            buffer.append(" ");
            right.printString(buffer, childrenPrefix, childrenPrefix, false);
        }
        if (!first && !unary) buffer.append(")");
    }

    /**
     * A method that indicated whether a node is a leaf.
     * @return A boolean variable indicating whether the node is a leaf.
     */
    public boolean isLeaf(){
        return left == null && right == null;
    }
}
