package hr.fer.zemris.zavrsni.evaluator.examples;

import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Arrays;

public class DTLZ1 extends MOOPProblem {

    private int k;

    /**
     * Constructs a new DTLZ1 optimization problem.
     *
     * @param fVectorLength - number of functions to be optimized
     * @param kVectorLength - length of the vector used in the g function
     */

    public DTLZ1(int fVectorLength, int kVectorLength) {
        objectives = new Function[fVectorLength];
        k = kVectorLength;
        lowerBounds = new double[getNumberOfVariables()];
        Arrays.fill(lowerBounds, 0);
        upperBounds = new double[getNumberOfVariables()];
        Arrays.fill(upperBounds, 1);
        Function g = RepeatingObjectives.cosineSum(k, 100, 0.5);
        for(int i = 0; i < fVectorLength; i++){
            final int k = i;
            objectives[i] = solution -> {
                int j;
                double[] x = solution.getVariables();
                double product = 0.5;
                for(j = 0; j < fVectorLength - k - 1; j++){
                    product *= x[j];
                }
                if(j < fVectorLength - 1){
                    product *= 1 - x[j];
                }
                product *= 1 + g.valueAt(solution);
                return product;
            };
        }
    }

    /**
     * Constructs a new DTLZ1 problem with the length of the vector for the g function of 5.
     * @param fVectorLength number of functions to be optimized
     */
    public DTLZ1(Integer fVectorLength) {
        this(fVectorLength, 5);
    }

    @Override public int getNumberOfVariables() {
        return objectives.length + k - 1;
    }

}
