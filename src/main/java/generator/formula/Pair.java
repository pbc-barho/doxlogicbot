package generator.Formula;

import java.io.Serial;
import java.io.Serializable;

/**
 * This class implements a directional pair of two values.
 */
public class Pair implements Serializable {

    @Serial
    private static final long serialVersionUID = 99L;

    private final Integer val0;
    private final Integer val1;

    /**
     * Constructor for a pair consisting of two values.
     * @param value0 The first value of the pair.
     * @param value1 The second value of the pair.
     */
    public Pair(Integer value0, Integer value1) {
        this.val0 = value0;
        this.val1 = value1;
    }

    /**
     * Getter for the first value of the pair.
     * @return The first value of the pair.
     */
    public Integer getValue0() {
        return this.val0;
    }

    /**
     * Getter for the second value of the pair.
     * @return The second value of the pair.
     */
    public Integer getValue1() {
        return this.val1;
    }
}
