package test.algorithm.NSGA3;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Util;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.BLXAlpha;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;
import test.easyproblems.EZProblem;

import java.util.List;

public class EZTest {

    public static void main(String[] args) {
        int numberOfDivisions = 4;
        int problemSize = 5;
        int distributionIndex = 4;

        double mutationChance = 0.08;
        double sigma = 0.1;
        double alpha = 0.1;

        int maxGen = 1000;
        boolean allowRepetition = false;

        MOOPProblem problem = new EZProblem(problemSize);

        SolutionFactory<Solution> s = new RegularSolutionFactory();
        List<Solution> population = MOOPUtils.generateRandomPopulation(
                NSGA3Util.getNumberOfReferencePoints(problem.getNumberOfObjectives(), numberOfDivisions),
                problem, s);
        Crossover<Solution> crossover = new BLXAlpha<>(s, alpha, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, sigma);

        NSGA3 nsga3 = new NSGA3(population, problem, crossover, mutation, maxGen, allowRepetition, numberOfDivisions);
        nsga3.run();
        MOOPUtils.printSolutions(nsga3);
    }
}
