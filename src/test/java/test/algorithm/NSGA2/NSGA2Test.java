package test.algorithm.NSGA2;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.NSGA2;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.BLXAlpha;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;
import test.easyproblems.EZProblem;

import java.util.List;

public class NSGA2Test {

    public static void main(String[] args) {

        final int populationSize = 100;

        final double blxAlpha       = 0.1;
        final double sigma          = 0.1;
        final double mutationChance = 0.08;

        final int     maxGen          = 1000;
        final boolean allowRepetition = false;
        final int tournamentSize = 8;

        MOOPProblem problem = new EZProblem(4);

        Solution[] population = MOOPUtils.generateRandomPopulation(populationSize, problem);
        Crossover crossover = new BLXAlpha(blxAlpha, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation  = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), sigma, mutationChance);

        AbstractMOOPAlgorithm nsga2 = new NSGA2(population, problem, crossover, mutation, tournamentSize, maxGen, allowRepetition);
        nsga2.run();

        MOOPUtils.printSolutions(nsga2);
    }
}
