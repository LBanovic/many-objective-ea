package hr.fer.zemris.zavrsni.evaluator.examples;

import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;

import java.util.Arrays;

public class DTLZ4 extends MOOPProblem {

    private int k;

    public DTLZ4(int fVectorLength, int kVectorLength) {
        this.k = kVectorLength;
        Function g = RepeatingObjectives.sumOfSquares(kVectorLength, 0.5);
        objectives = RepeatingObjectives.spherePareto(fVectorLength, g, 100);
        this.lowerBounds = new double[getNumberOfVariables()];
        Arrays.fill(lowerBounds, 0);
        this.upperBounds = new double[getNumberOfVariables()];
        Arrays.fill(upperBounds, 1);
    }

    public DTLZ4(Integer fVectorLength) {
        this(fVectorLength, 10);
    }

    @Override
    public int getNumberOfVariables() {
        return objectives.length + k - 1;
    }
}
