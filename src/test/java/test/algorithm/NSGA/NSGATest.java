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
import hr.fer.zemris.zavrsni.evaluator.Function;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;
import test.easyproblems.EZProblem;

import java.util.List;

public class NSGATest {

    public static void main(String[] args) {

        final int populationSize = 100;

        final double blxAlpha = 0.1;
        final double sigma = 0.1;
        final double mutationChance = 0.08;

        final int maxGen = 1000;
        final boolean allowRepetition = false;
        final double epsilon = 0.1;
        final double sigmaShare = 4;
        final double alpha = 2;

        MOOPProblem problem = new EZProblem(4);

        Solution[] population = MOOPUtils.generateRandomPopulation(populationSize, problem);
        Crossover crossover = new BLXAlpha(blxAlpha, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), sigma, mutationChance);
        Selection selection = new RouletteWheelSelection();

        AbstractMOOPAlgorithm nsga = new NSGA(population, problem, crossover, selection, mutation, maxGen, allowRepetition,
                                                            epsilon,
                                                            sigmaShare,
                                                            alpha
        );

        nsga.run();
        MOOPUtils.printSolutions(nsga);
    }
}
