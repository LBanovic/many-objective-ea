package test.algorithm.NSGA2;

import hr.fer.zemris.zavrsni.algorithms.NSGA2;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.BLXAlpha;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.FitnessSolutionFactory;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;
import hr.fer.zemris.zavrsni.evaluator.examples.EZProblem;

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

        SolutionFactory<FitnessSolution<Double>> f = new FitnessSolutionFactory<>();

        List<FitnessSolution<Double>> population = PopulationUtils.generateRandomPopulation(populationSize, problem, f);
        Crossover<FitnessSolution<Double>> crossover = new BLXAlpha<>(f, blxAlpha, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation  = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), sigma, mutationChance);

        NSGA2 nsga2 = new NSGA2(population, problem, crossover, mutation, tournamentSize, maxGen, allowRepetition);
        nsga2.run();

        OutputUtils.printSolutions(nsga2);
    }
}
