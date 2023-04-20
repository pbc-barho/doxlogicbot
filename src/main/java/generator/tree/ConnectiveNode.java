package generator.Tree;

import generator.connectives.Connective;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * A class that implements a node that contains a connective of a formula.
 */
@Getter
public class ConnectiveNode extends Node implements Serializable {

    @Serial
    private static final long serialVersionUID = 19L;

    private final Connective value;

    /**
     * Constructor of a node that consists of a connective.
     * @param connective The connective that the node consists of.
     */
    public ConnectiveNode(Connective connective) {
        this.value = connective;
    }

    @Override
    public String printValue() {
        return value.getSymbol();
    }
}

