package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.NSGA;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

public class DummyFitnessProvider implements ValueProvider<Double> {

    private double[] lowerBounds, upperBounds;
    private double epsilon, sigmaShare, alpha;
    private AbstractMOOPAlgorithm moop;

    public DummyFitnessProvider(
        double[] lowerBounds,
        double[] upperBounds,
        double epsilon,
        double sigmaShare,
        double alpha,
        AbstractMOOPAlgorithm moop
    ) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.epsilon = epsilon;
        this.sigmaShare = sigmaShare;
        this.alpha = alpha;
        this.moop = moop;
    }

    public void provide(Map<Solution, Double> dummyFitness){
        double Fmin = moop.populationSize() + epsilon;
        List<List<Solution>> fronts = moop.getParetoFronts();
        for (List<Solution> front : fronts) {
            for (Solution q : front) {
                dummyFitness.put(q,(Fmin - epsilon) / nicheDensity(q, front));
            }
            Fmin = dummyFitness.get(Collections.min(front, Comparator.comparingDouble(dummyFitness::get)));
        }
    }

    private double nicheDensity(Solution sol, List<Solution> front) {
        double nicheDensity = 0;
        for (Solution solution : front) {
            nicheDensity += sharingFunction(sol, solution);
        }
        return nicheDensity;
    }

    private double sharingFunction(Solution i, Solution j) {
        double distance = distanceFunction(i, j, lowerBounds, upperBounds);
        if (distance > sigmaShare) {
            return 0;
        } else {
            return (1 - Math.pow(distance / sigmaShare, alpha));
        }
    }

    private static double distanceFunction(Solution i, Solution j, double[] lowerBounds, double[] upperBounds) {
        double[] var1 = i.getVariables();
        double[] var2 = j.getVariables();

        double distance = 0;

        for (int k = 0; k < var1.length; k++) {
            double nextAddition = (var1[k] - var2[k]) / (upperBounds[k] - lowerBounds[k]);
            distance += nextAddition * nextAddition;
        }

        return Math.sqrt(distance);
    }
}
