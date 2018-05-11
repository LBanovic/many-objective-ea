package test.algorithm.MOEAD.TCH;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.moead.AbstractMOEAD;
import hr.fer.zemris.zavrsni.algorithms.moead.MOEAD_TCH;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.BLXAlpha;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.PolynomialMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;
import test.easyproblems.EZProblem;

import java.util.List;

public class MOEAD_TCH_EZTest {
    public static void main(String[] args) {
        MOOPProblem problem = new EZProblem(4);

        int parameterH = 12;
        int closestVectors = 10;
        final int populationSize = AbstractMOEAD.getPreferredPopulationSize(parameterH, problem.getNumberOfObjectives());
        final double eta = 20;
        final double mutationChance = 1. / problem.getNumberOfVariables();

        final int maxGen = 250;

        SolutionFactory<Solution> s = new RegularSolutionFactory();
        List<Solution> population = PopulationUtils.generateRandomPopulation(populationSize, problem, s);
        Crossover<Solution> crossover = new SBXCrossover<>(s, eta, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new PolynomialMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, eta);
        AbstractMOOPAlgorithm<Solution> moead = new MOEAD_TCH(population, problem, closestVectors, parameterH, mutation, crossover, maxGen);

        moead.run();
        OutputUtils.printSolutions(moead);
    }
}
