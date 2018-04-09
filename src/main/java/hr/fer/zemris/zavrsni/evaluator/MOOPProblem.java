package hr.fer.zemris.zavrsni.evaluator;

import hr.fer.zemris.zavrsni.solution.Solution;

/**
 * Class that describes a MOOPProblem. By default, all objectives are to be minimized. If that needs to be changed,
 * implementation needs to override the evaluateObjectives method.
 */
public abstract class MOOPProblem {

    protected Function[] objectives;
    protected double[] lowerBounds;
    protected double[] upperBounds;

    /**
     * Calculates all the objectives at the specified Solution.
     * @param solution
     */
    public void evaluateObjectives(Solution solution){
        double[] objectives = solution.getObjectives();
        if (objectives.length != this.objectives.length) {
            throw new RuntimeException("Array not the required dimension");
        }
        for (int i = 0; i < objectives.length; i++) {
            objectives[i] = -this.objectives[i].valueAt(solution);
        }
    }

    /**
     * Returns the lower bounds of the domains of the objective functions in order of their addition to the problem.
     * The number of bounds should be equal to the number of variables.
     * @return
     */
    public double[] getLowerBounds(){
        return lowerBounds;
    }

    /**
     * Returns the upper bounds of the domains of the objective functions in order of their addition to the problem.
     * The number of bounds should be equal to the number of variables.
     * @return
     */
    public double[] getUpperBounds(){
        return upperBounds;
    }

    /**
     * Returns the given number of objectives.
     * @return
     */
    public int getNumberOfObjectives(){
        return objectives.length;
    }

    /**
     * Returns the required number of variables.
     * @return
     */
    public abstract int getNumberOfVariables();
}
