package test.algorithm.MOEAD.TCH;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.moead.AbstractMOEAD;
import hr.fer.zemris.zavrsni.algorithms.moead.MOEAD_TCH;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.PolynomialMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.experiments.MOEAD_TCHExperiment;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.List;

public class MOEAD_TCH_DTLZ1Test {
    public static void main(String[] args) {

        MOOPProblem problem = new DTLZ1(3);

        String parameterH = "12";
        String closestVectors = "10";
        final int populationSize = AbstractMOEAD.getPreferredPopulationSize(Integer.parseInt(parameterH), problem.getNumberOfObjectives());
;
        List<Solution> population = PopulationUtils.generateRandomPopulation(populationSize, problem);

        int maxGen = 250;

        AbstractMOOPAlgorithm<Solution> moead = new MOEAD_TCHExperiment().run(problem,
                population, maxGen, closestVectors, parameterH);
        OutputUtils.printSolutions(moead);
    }
}
