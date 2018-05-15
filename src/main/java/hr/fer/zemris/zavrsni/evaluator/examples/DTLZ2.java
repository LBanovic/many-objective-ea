package hr.fer.zemris.zavrsni.evaluator.examples;

import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;

import java.util.Arrays;

public class DTLZ2 extends MOOPProblem {

    private int k;

    public DTLZ2(int fVectorLength, int kVectorLength){
        objectives = new Function[fVectorLength];
        k = kVectorLength;
        lowerBounds = new double[getNumberOfVariables()];
        Arrays.fill(lowerBounds, 0);
        upperBounds = new double[getNumberOfVariables()];
        Arrays.fill(upperBounds, 1);
        Function g = solution -> {
            double[] x = solution.getVariables();
            double sum = 0;
            for(int i = x.length - k; i < x.length; i++){
                sum += (x[i] - 0.5) * (x[i] - 0.5);
            }
            return sum;
        };

        for(int i = 0; i < fVectorLength; i++){
            final int k = i;
            objectives[i] = solution -> {
                int j;
                double[] x = solution.getVariables();
                double product = 1;
                for(j = 0; j < fVectorLength - k - 1; j++){
                    product *= Math.cos(x[j] * Math.PI / 2);
                }
                if(j < fVectorLength - 1){
                    product *= Math.sin(x[j] * Math.PI / 2);
                }
                product *= 1 + g.valueAt(solution);
                return product;
            };
        }
    }

    public DTLZ2(Integer fVectorLength){
        this(fVectorLength, 10);
    }

    @Override public int getNumberOfVariables() {
        return objectives.length - 1 + k;
    }
}
