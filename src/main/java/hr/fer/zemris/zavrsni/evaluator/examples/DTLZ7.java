package hr.fer.zemris.zavrsni.evaluator.examples;

import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;

import java.util.Arrays;

public class DTLZ7 extends MOOPProblem {

    private int k;

    public DTLZ7(int fVectorLength, int kVectorLength){
        k = kVectorLength;
        objectives = new Function[fVectorLength];
        Function g = solution -> {
            double[] x = solution.getVariables();
            double sum = 0;
            for (int i = x.length - kVectorLength; i < x.length; i++) {
                sum += x[i];
            }
            sum *= 9. / kVectorLength;
            return sum + 1;
        };

        for(int i = 0; i < objectives.length - 1; i++){
            final int k = i;
            objectives[i] = solution -> solution.getVariables()[k];
        }

        Function h = solution -> {
            double sum = 0;
            for(int i = 0; i < objectives.length - 1; i++){
                double f = objectives[i].valueAt(solution);
                sum += f / (1 + g.valueAt(solution)) * (1 + Math.sin(3 * Math.PI * f));
            }
            return objectives.length - sum;
        };

        objectives[objectives.length - 1] = solution -> (1 + g.valueAt(solution)) * h.valueAt(solution);

        upperBounds = new double[getNumberOfVariables()];
        Arrays.fill(upperBounds, 1);
        lowerBounds = new double[getNumberOfVariables()];
    }

    public DTLZ7(Integer fVectorLength){
        this(fVectorLength, 20);
    }

    @Override
    public int getNumberOfVariables() {
        return objectives.length + k - 1;
    }

}
