package hr.fer.zemris.zavrsni.evaluator.examples;

import hr.fer.zemris.zavrsni.evaluator.Function;

public class RepeatingObjectives {

    static Function[] spherePareto(int fVectorLength, Function g, double alpha) {
        Function[] objectives = new Function[fVectorLength];
        for (int i = 0; i < fVectorLength; i++) {
            final int k = i;
            objectives[i] = solution -> {
                int j;
                double[] x = solution.getVariables();
                double product = 1;
                for (j = 0; j < fVectorLength - k - 1; j++) {
                    product *= Math.cos(Math.pow(x[j], alpha) * Math.PI / 2);
                }
                if (j < fVectorLength - 1) {
                    product *= Math.sin(Math.pow(x[j], alpha) * Math.PI / 2);
                }
                product *= 1 + g.valueAt(solution);
                return product;
            };
        }
        return objectives;
    }

    static Function[] spherePareto(int fVectorLength, Function g) {
        return spherePareto(fVectorLength, g, 1);
    }

    static Function[] curvePareto(int fVectorLength, Function g) {
        Function[] objectives = new Function[fVectorLength];
        for (int i = 0; i < fVectorLength; i++) {
            final int k = i;
            objectives[i] = solution -> {
                int j;
                double[] x = solution.getVariables();
                double[] theta = new double[x.length];
                theta[0] = x[0] * Math.PI / 2;
                double t = Math.PI / (4 * (1 + g.valueAt(solution)));
                for (int z = 1; z < theta.length; z++) {
                    theta[z] = t * (1 + 2 * g.valueAt(solution) * x[z]);
                }
                double product = 1;
                for (j = 0; j < fVectorLength - k - 1; j++) {
                    product *= Math.cos(theta[j]);
                }
                if (j < fVectorLength - 1) {
                    product *= Math.sin(theta[j]);
                }
                product *= 1 + g.valueAt(solution);
                return product;
            };
        }
        return objectives;
    }

    static Function sumOfSquares(int k, double y) {
        return solution -> {
            double[] x = solution.getVariables();
            double sum = 0;
            for (int i = x.length - k; i < x.length; i++) {
                sum += (x[i] - y) * (x[i] - y);
            }
            return sum;
        };
    }

    static Function cosineSum(int kVectorLength, double multiplier, double y) {
        return solution -> {
            double[] x = solution.getVariables();
            double sum = kVectorLength;
            for (int i = x.length - kVectorLength; i < x.length; i++) {
                sum += (x[i] - y) * (x[i] - y) - Math.cos(20 * Math.PI * (x[i] - y));
            }
            sum *= multiplier;
            return sum;
        };
    }
}
