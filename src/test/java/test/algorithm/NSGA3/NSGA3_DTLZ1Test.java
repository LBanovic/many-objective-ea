package test.algorithm.NSGA3;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.experiments.NSGA3Experiment;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.List;

public class NSGA3_DTLZ1Test {

    public static void main(String[] args) {

        String numberOfDivisions = "12";
        int problemSize = 3;

        MOOPProblem problem = new DTLZ1(problemSize);
        String allowRepetition = "false";
        int maxGen = 250;

        List<Solution> population = PopulationUtils.generateRandomPopulation(
                NSGA3.getPreferredPopulationSize(problem.getNumberOfObjectives(),
                        Integer.parseInt(numberOfDivisions)),
                problem);
        AbstractMOOPAlgorithm<Solution> nsga3 = new NSGA3Experiment().run(problem, population, maxGen, numberOfDivisions, allowRepetition);
        OutputUtils.printSolutions(nsga3);
    }
}
