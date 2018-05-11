package test.algorithm.SPEA2;

import hr.fer.zemris.zavrsni.algorithms.*;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.FitnessSolutionFactory;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.List;

public class SPEA2_DTLZ1Test {
    public static void main(String[] args) {
        MOOPProblem problem = new DTLZ1(5);

        final int populationSize = 100;
        final int archiveSize = 100;
        final double eta = 20;
        final double sigma = 0.1;
        final double mutationChance = 0.05;
        final boolean allowRepetition = false;
        final int tournamentSize = 2;

        final int maxGen = 1000;

        SolutionFactory<FitnessSolution<Double>> s = new FitnessSolutionFactory<>();
        List<FitnessSolution<Double>> population = PopulationUtils.generateRandomPopulation(populationSize, problem, s);
        Crossover<FitnessSolution<Double>> crossover = new SBXCrossover<>(s, eta, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), sigma, mutationChance);

        AbstractMOOPAlgorithm<FitnessSolution<Double>> spea2 = new SPEA2(population, problem, maxGen, archiveSize,
                crossover, mutation, tournamentSize, allowRepetition);
        spea2.run();

        OutputUtils.printSolutions(spea2);
    }
}
