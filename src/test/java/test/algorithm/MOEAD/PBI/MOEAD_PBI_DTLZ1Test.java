package test.algorithm.MOEAD.PBI;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.moead.AbstractMOEAD;
import hr.fer.zemris.zavrsni.algorithms.moead.MOEAD_PBI;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.PolynomialMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.List;

public class MOEAD_PBI_DTLZ1Test {
    public static void main(String[] args) {
        MOOPProblem problem = new DTLZ1(3);

        int parameterH = 12;
        int closestVectors = 10;
        final int populationSize = AbstractMOEAD.getPreferredPopulationSize(parameterH, problem.getNumberOfObjectives());
        final double eta = 20;
        final double mutationChance = 1. / problem.getNumberOfVariables();
        final double penalty = 5;

        final int maxGen = 250;

        SolutionFactory<Solution> s = new RegularSolutionFactory();
        List<Solution> population = PopulationUtils.generateRandomPopulation(populationSize, problem, s);
        Crossover<Solution> crossover = new SBXCrossover<>(s, eta, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new PolynomialMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, eta);

        AbstractMOOPAlgorithm<Solution> moead = new MOEAD_PBI(population, problem, closestVectors, parameterH, mutation, crossover, maxGen, penalty);
        moead.run();

        OutputUtils.printSolutions(moead);
    }
}
