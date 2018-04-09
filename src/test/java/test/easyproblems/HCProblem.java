package test.easyproblems;

import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

public class HCProblem extends MOOPProblem {

    public HCProblem() {
        LinearFunction f = new LinearFunction();
        objectives = new Function[2];
        objectives[0] = f;
        objectives[1] = new Inverse(f);
        lowerBounds = new double[]{0.1, 0};
        upperBounds = new double[]{1, 5};
    }

    @Override public int getNumberOfVariables() {
        return 2;
    }


    private static class LinearFunction implements Function {
        @Override public double valueAt(Solution solution) {
            return solution.getVariables()[0];
        }
    }

    private static class Inverse implements Function {
        LinearFunction f;

        public Inverse(LinearFunction f) {
            this.f = f;
        }

        @Override public double valueAt(Solution solution) {
            return (1 + solution.getVariables()[1]) / f.valueAt(solution);
        }
    }
}
