package hr.fer.zemris.zavrsni.evaluator;

import hr.fer.zemris.zavrsni.solution.Solution;

public interface Function {
    /**
     * Returns the value of the function at the specified point.
     * @param solution point of interest
     * @return value of the function
     */
    public double valueAt(Solution solution);
}
