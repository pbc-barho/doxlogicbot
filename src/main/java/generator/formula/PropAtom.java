package generator.Formula;

import lombok.Getter;

/**
 * An enumeration representing the three propositional atoms used for the formula generation.
 */
@Getter
public enum PropAtom {
    P("p", 1),
    Q("q", 2),
    R("r",3);

    private final int priority;
    private final String symbol;

    /**
     * Constructor for the propositional atoms.
     * @param symbol The printed letter representing the propositional atom.
     * @param priority The priority for using that propositional atom in the formula generation.
     */
    PropAtom(String symbol, int priority) {
        this.priority = priority;
        this.symbol = symbol;
    }
}
