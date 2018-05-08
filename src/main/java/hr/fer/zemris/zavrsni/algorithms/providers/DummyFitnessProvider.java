package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DummyFitnessProvider implements ValueProvider<Double> {

    private double[] lowerBounds, upperBounds;
    private double epsilon, sigmaShare, alpha;
    private AbstractMOOPAlgorithm<FitnessSolution<Double>> moop;

    public DummyFitnessProvider(
        double[] lowerBounds,
        double[] upperBounds,
        double epsilon,
        double sigmaShare,
        double alpha,
        AbstractMOOPAlgorithm<FitnessSolution<Double>> moop
    ) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.epsilon = epsilon;
        this.sigmaShare = sigmaShare;
        this.alpha = alpha;
        this.moop = moop;
    }

    @Override
    public void provide(List<FitnessSolution<Double>> population) {
        double Fmin = moop.populationSize() + epsilon;
        List<List<FitnessSolution<Double>>> fronts = moop.getParetoFronts();
        for (List<FitnessSolution<Double>> front : fronts) {
            for (FitnessSolution<Double> f : front) {
                f.setFitness((Fmin - epsilon) / nicheDensity(f, front));
            }
           Fmin = Collections.min(front, Comparator.naturalOrder()).getFitness();
        }
    }

    private double nicheDensity(Solution sol, List<FitnessSolution<Double>> front) {
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
