package test.algorithm.NSGA2;

import hr.fer.zemris.zavrsni.algorithms.NSGA2;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.PolynomialMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.FitnessSolutionFactory;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;
import hr.fer.zemris.zavrsni.evaluator.examples.HCProblem;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

public class NSGA2TestHarder {
    public static void main(String[] args) throws FileNotFoundException {
        final int populationSize = 100;

        final double blxAlpha       = 0.2;
        final double sigma          = 0.2;
        final double mutationChance = 0.08;

        final int     maxGen          = 1000;
        final boolean allowRepetition = false;
        final int     tournamentSize  = 5;

        final double eta = 20;
        MOOPProblem problem = new DTLZ1(5);

        SolutionFactory<FitnessSolution<Double>> f = new FitnessSolutionFactory<>();

        List<FitnessSolution<Double>> population = PopulationUtils.generateRandomPopulation(populationSize, problem, f);
        Crossover<FitnessSolution<Double>> crossover = new SBXCrossover<>(f, eta, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation  = new PolynomialMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, eta);

        NSGA2 nsga2 = new NSGA2(population, problem, crossover, mutation, tournamentSize, maxGen, allowRepetition);
        nsga2.run();

        OutputUtils.printParameters(nsga2, System.out);
        System.out.println();
        OutputUtils.printSolutions(nsga2);
    }
}
