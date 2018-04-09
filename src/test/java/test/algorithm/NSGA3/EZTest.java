package test.algorithm.NSGA3;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Util;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.BLXAlpha;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;
import test.easyproblems.EZProblem;

public class EZTest {

    public static void main(String[] args) {
        int numberOfDivisions = 4;
        int problemSize = 5;

        double mutationChance = 0.08;
        double sigma = 0.1;
        double alpha = 0.1;

        int maxGen = 1000;
        boolean allowRepetition = false;

        MOOPProblem problem = new EZProblem(problemSize);
        Solution[] population = MOOPUtils.generateRandomPopulation(
                NSGA3Util.getNumberOfReferencePoints(problem.getNumberOfObjectives(), numberOfDivisions),
                problem);
        Crossover crossover = new BLXAlpha(alpha, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, sigma);

        AbstractMOOPAlgorithm nsga3 = new NSGA3(population, problem, crossover, mutation, maxGen, allowRepetition, numberOfDivisions);
        nsga3.run();
        MOOPUtils.printSolutions(nsga3);

    }
}
