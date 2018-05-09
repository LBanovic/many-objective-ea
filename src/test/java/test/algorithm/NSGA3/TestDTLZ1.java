package test.algorithm.NSGA3;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Util;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.List;

public class TestDTLZ1 {

    public static void main(String[] args) {

        int numberOfDivisions = 12;
        int problemSize = 5;

        double eta = 30;

        MOOPProblem problem = new DTLZ1(problemSize);

        double mutationChance = 1. / problem.getNumberOfVariables();
        double sigma = 0.1;

        int maxGen = 1000;
        boolean allowRepetition = false;

        SolutionFactory<Solution> s = new RegularSolutionFactory();
        List<Solution> population = MOOPUtils.generateRandomPopulation(
                NSGA3Util.getNumberOfReferencePoints(problem.getNumberOfObjectives(), numberOfDivisions),
                problem, s);
        Crossover<Solution> crossover = new SBXCrossover<>(s, eta, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, sigma);

        NSGA3 nsga3 = new NSGA3(population, problem, crossover, mutation, maxGen, allowRepetition, numberOfDivisions);
        nsga3.run();
        MOOPUtils.printSolutions(nsga3);
    }
}
