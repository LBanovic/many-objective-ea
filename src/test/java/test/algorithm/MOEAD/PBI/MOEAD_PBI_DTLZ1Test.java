package test.algorithm.MOEAD.PBI;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.moead.AbstractMOEAD;
import hr.fer.zemris.zavrsni.algorithms.moead.MOEAD_PBI;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.experiments.MOEAD_PBIExperiment;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.List;

public class MOEAD_PBI_DTLZ1Test {
    public static void main(String[] args) {
        MOOPProblem problem = new DTLZ1(3);

        String parameterH = "12";
        String closestVectors = "10";
        final int populationSize = AbstractMOEAD.getPreferredPopulationSize(Integer.parseInt(parameterH), problem.getNumberOfObjectives());
        final String penalty = "5";

        int maxGen = 250;

        List<Solution> population = PopulationUtils.generateRandomPopulation(populationSize, problem);

        AbstractMOOPAlgorithm<Solution> moead = new MOEAD_PBIExperiment().run(problem, population, maxGen,
                closestVectors, parameterH, penalty);

        OutputUtils.printSolutions(moead);
    }
}
