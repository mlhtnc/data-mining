package neuroevolution;

/**
 *
 * @author tnc
 */
public enum FitnessType {
    // If output in a shape that 1x1, we can use this fitness type.
    // It just get difference between target and output and then abs.
    // We see problem as a maximization problem so we just extract from one.
    // e.g.  1 - abs(target - output)
    ABS_DIFFERENCE,
    
    // We look if max one of the target and the output is the same, if yes,
    // just increase the fitness.
    MAX_ONE
}
