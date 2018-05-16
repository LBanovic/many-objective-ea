package test.algorithm.NSGA;

import hr.fer.zemris.zavrsni.algorithms.*;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.experiments.NSGAExperiment;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.FitnessSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.List;

public class NSGATest {

    public static void main(String[] args) {

        final int populationSize = 100;

        final String allowRepetition = "false";
        final String epsilon = "0.1";
        final String sigmaShare = "4";
        final String alpha = "2";

        MOOPProblem problem = null;

        try {
            problem = MOOPUtils.getExample("EZProblem", 4);
        }catch(Exception e){
            System.err.println("Invalid problem name.");
            System.exit(0);
        }

        List<Solution> population = PopulationUtils.generateRandomPopulation(populationSize, problem);

        int maxGen = 250;

        AbstractMOOPAlgorithm<FitnessSolution<Double>> nsga = new NSGAExperiment().run(problem, population,
                maxGen, epsilon, sigmaShare, alpha, allowRepetition);
        OutputUtils.printSolutions(nsga);
    }
}
