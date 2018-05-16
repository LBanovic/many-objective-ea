package test.algorithm.SPEA2;

import hr.fer.zemris.zavrsni.algorithms.*;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.PolynomialMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.experiments.SPEA2Experiment;
import hr.fer.zemris.zavrsni.solution.*;

import java.util.List;

public class SPEA2_DTLZ1Test {
    public static void main(String[] args) {
        MOOPProblem problem = new DTLZ1(3);

        final int populationSize = 100;
        final int maxGen = 250;
        final String archiveSize = "100";
        final String allowRepetition = "false";
        final String tournamentSize = "2";

        List<Solution> population = PopulationUtils.generateRandomPopulation(populationSize, problem);

        AbstractMOOPAlgorithm<FitnessSolution<Double>> spea2 = new SPEA2Experiment().run(problem, population, maxGen, archiveSize, tournamentSize, allowRepetition);

        OutputUtils.printSolutions(spea2);
//        MOOPProblem problem = new DTLZ1(3);
//
//        final int populationSize = 100;
//        final int archiveSize = 100;
//        final double eta = 20;
//        final double sigma = 0.1;
//        final double mutationChance = 1. / problem.getNumberOfVariables();
//        final boolean allowRepetition = false;
//        final int tournamentSize = 2;
//
//        final int maxGen = 250;
//
//        SolutionFactory<FitnessSolution<Double>> s = new FitnessSolutionFactory<>();
//        List<FitnessSolution<Double>> population = PopulationUtils.generateRandomPopulation(populationSize, problem, s);
//        Crossover<FitnessSolution<Double>> crossover = new SBXCrossover<>(s, eta, problem.getLowerBounds(), problem.getUpperBounds());
//        Mutation mutation = new PolynomialMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, eta  );
//
//        AbstractMOOPAlgorithm<FitnessSolution<Double>> spea2 = new SPEA2(population, problem, maxGen, archiveSize,
//                crossover, mutation, tournamentSize, allowRepetition);
//        spea2.run();
//
//        OutputUtils.printSolutions(spea2);

    }
}
