package hr.fer.zemris.zavrsni.evaluator.examples;

import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Arrays;

public class EZProblem extends MOOPProblem {

    private int k;

    public EZProblem(Integer k) {
        this.k = k;
        Function[] q = new Function[k];
        for(int i = 0; i < k; i++){
            q[i] = new Square(i);
        }
        this.objectives = q;
        lowerBounds = new double[k];
        upperBounds = new double[k];
        Arrays.fill(lowerBounds, -5);
        Arrays.fill(upperBounds, 5);
    }

    @Override public int getNumberOfVariables() {
        return k;
    }

    private static class Square implements Function {
        int index;

        public Square(int index) {
            this.index = index;
        }

        @Override public double valueAt(Solution solution) {
            return solution.getVariables()[index] * solution.getVariables()[index];
        }
    }
}


