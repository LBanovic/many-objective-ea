package test.algorithm.NSGA;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.NSGA;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.BLXAlpha;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.RouletteWheelSelection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.FitnessSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;
import test.easyproblems.HCProblem;

import java.util.List;

public class NSGATestHarder {

    public static void main(String[] args) {
        final int populationSize = 100;

        final double blxAlpha = 0.2;
        final double sigma = 0.2;
        final double mutationChance = 0.08;

        final int maxGen = 1000;
        final boolean allowRepetition = false;
        final double epsilon = 0.1;
        final double sigmaShare = 3;
        final double alpha = 2;

        MOOPProblem problem = new HCProblem();

        SolutionFactory<FitnessSolution<Double>> f = new FitnessSolutionFactory<>();
        List<FitnessSolution<Double>> population = MOOPUtils.generateRandomPopulation(populationSize, problem, f);
        Crossover<FitnessSolution<Double>> crossover = new BLXAlpha<>(f, blxAlpha, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), sigma, mutationChance);
        RouletteWheelSelection selection = new RouletteWheelSelection();

        NSGA nsga = new NSGA(population, problem, crossover, selection, mutation, maxGen, allowRepetition,
                epsilon,
                sigmaShare,
                alpha
        );
        nsga.run();

        MOOPUtils.printParameters(nsga);
        System.out.println();
        MOOPUtils.printSolutions(nsga);
    }
}
