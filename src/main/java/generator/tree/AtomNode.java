package generator.tree;

import generator.formula.PropAtom;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class represents a node on a tableau that consists of a propositional atom.
 */
public class AtomNode extends Node implements Serializable {

    @Serial
    private static final long serialVersionUID = 18L;

    private final PropAtom value;

    /**
     * Constructor for the node consisting of a propositional atom.
     * @param value The propositional atom on the node.
     */
    public AtomNode(PropAtom value) {
        this.value = value;
    }

    @Override
    public String printValue() {
        return value.getSymbol();
    }

    @Override
    public String getValue() {
        return value.getSymbol();
    }
}
