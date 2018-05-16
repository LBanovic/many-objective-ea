package hr.fer.zemris.zavrsni.evaluator.examples;

import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;

import java.util.Arrays;

public class DTLZ6 extends MOOPProblem{
    private int k;

    public DTLZ6(int fVectorLength, int kVectorLength){
        k = kVectorLength;
        Function g = solution -> {
            double sum = 0;
            double[] x = solution.getVariables();
            for (int i = x.length - kVectorLength; i < x.length; i++) {
                sum += Math.pow(x[i], 0.1);
            }
            return sum;
        };
        objectives = RepeatingObjectives.curvePareto(fVectorLength, g);
        upperBounds = new double[getNumberOfVariables()];
        Arrays.fill(upperBounds, 1);
        lowerBounds = new double[getNumberOfVariables()];
    }

    public DTLZ6(Integer fVectorLength){
        this(fVectorLength, 10);
    }

    @Override
    public int getNumberOfVariables() {
        return objectives.length + k - 1;
    }
}
