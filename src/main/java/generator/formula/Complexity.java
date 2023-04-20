package generator.formula;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * A class representing the complexity of a formula measured by the modal depth and the number of connectives.
 */
@Getter @Setter
public class Complexity implements Serializable {

    @Serial
    private static final long serialVersionUID = 14L;
    private int modalDepth;
    private int nrConnectives;

    /**
     * The constructor of the complexity.
     * @param modalDepth The modal depth of the corresponding formula.
     * @param nrConnectives The number of connectives in the formula.
     */
    public Complexity(int modalDepth, int nrConnectives){
        this.modalDepth = modalDepth;
        this.nrConnectives = nrConnectives;
    }
}
