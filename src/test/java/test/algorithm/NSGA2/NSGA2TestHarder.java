package test.algorithm.NSGA2;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.NSGA2;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.BLXAlpha;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.FitnessSolutionFactory;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;
import test.easyproblems.HCProblem;

import java.util.List;

public class NSGA2TestHarder {
    public static void main(String[] args) {
        final int populationSize = 100;

        final double blxAlpha       = 0.2;
        final double sigma          = 0.2;
        final double mutationChance = 0.08;

        final int     maxGen          = 1000;
        final boolean allowRepetition = false;
        final int     tournamentSize  = 8;

        final double eta = 20;
        MOOPProblem problem = new HCProblem();

        SolutionFactory<FitnessSolution<Double>> f = new FitnessSolutionFactory<>();

        List<FitnessSolution<Double>> population = MOOPUtils.generateRandomPopulation(populationSize, problem, f);
        Crossover<FitnessSolution<Double>> crossover = new SBXCrossover<>(f, eta, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation  = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), sigma, mutationChance);

        NSGA2 nsga2 = new NSGA2(population, problem, crossover, mutation, tournamentSize, maxGen, allowRepetition);
        nsga2.run();

        MOOPUtils.printParameters(nsga2);
        System.out.println();
        MOOPUtils.printSolutions(nsga2);
    }
}
