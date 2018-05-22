package test.algorithm.NSGA3;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.PolynomialMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.experiments.NSGA3Experiment;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NSGA3_DTLZ1Test {

    public static void main(String[] args) {

        String numberOfDivisions = "6";
        int problemSize = 5;

        MOOPProblem problem = new DTLZ1(problemSize);
        String allowRepetition = "false";
        int maxGen = 250;

        List<Solution> population = PopulationUtils.generateRandomPopulation(
                NSGA3.getPreferredPopulationSize(problem.getNumberOfObjectives(),
                        Collections.singletonList(Integer.parseInt(numberOfDivisions))),
                problem);
        AbstractMOOPAlgorithm<Solution> nsga3 = new NSGA3Experiment().run(problem, population, maxGen, numberOfDivisions, allowRepetition);
        OutputUtils.printSolutions(nsga3);

//        int divs = 6;
//        List<Integer> numberOfDivisions = Collections.singletonList(divs);
//
//        int problemSize = 5;
//
//        double eta = 30;
//
//        MOOPProblem problem = new DTLZ1(problemSize);
//
//        double mutationChance = 1. / problem.getNumberOfVariables();
//        double sigma = 0.1;
//
//        int maxGen = 350;
//        boolean allowRepetition = false;
//
//        List<Solution> population = PopulationUtils.generateRandomPopulation(
//                NSGA3.getPreferredPopulationSize(problem.getNumberOfObjectives(), numberOfDivisions),
//                problem);
//        System.out.println(NSGA3.getPreferredPopulationSize(problem.getNumberOfObjectives(), numberOfDivisions));
//        SolutionFactory<Solution> s = new RegularSolutionFactory();
//        Crossover<Solution> crossover = new SBXCrossover<>(s, eta, problem.getLowerBounds(), problem.getUpperBounds());
//        Mutation mutation = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, sigma);
//
//        AbstractMOOPAlgorithm<Solution> nsga3 = new NSGA3(population, problem, crossover, mutation, maxGen, allowRepetition, numberOfDivisions);
//        nsga3.run();
//        OutputUtils.printSolutions(nsga3);
    }
}
