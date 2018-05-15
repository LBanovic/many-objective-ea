package hr.fer.zemris.zavrsni.evaluator.examples;

import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;

import java.util.Arrays;

public class DTLZ3 extends MOOPProblem {

    private int k;

    public DTLZ3(int fVectorLength, int kVectorLength){
        Function g = RepeatingObjectives.cosineSum(k, 100, 0.5);
        objectives = RepeatingObjectives.spherePareto(fVectorLength, g);
        k = kVectorLength;
        lowerBounds = new double[getNumberOfVariables()];
        Arrays.fill(lowerBounds, 0);
        upperBounds = new double[getNumberOfVariables()];
        Arrays.fill(upperBounds, 1);
    }

    public DTLZ3(Integer fVectorLength){
        this(fVectorLength, 10);
    }

    @Override
    public int getNumberOfVariables() {
        return objectives.length + k - 1;
    }
}
