package test.algorithm.NSGA3;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.NSGA2;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Util;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.BLXAlpha;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Arrays;
import java.util.List;

public class TestDTLZ1 {

    public static void main(String[] args) {

        int numberOfDivisions = 11;
        int problemSize = 3;
        int distributionIndex = 20;


        MOOPProblem problem = new DTLZ1(problemSize);

        double mutationChance = 1./problem.getNumberOfVariables();
        double sigma = 0.08;
        double alpha = 0.08;

        int maxGen = 1000;
        int populationSize = 100;
        int tournamentSize = 8;
        boolean allowRepetition = false;


        Solution[] population = MOOPUtils.generateRandomPopulation(
                NSGA3Util.getNumberOfReferencePoints(problem.getNumberOfObjectives(), numberOfDivisions),
                problem);
        Crossover crossover = new BLXAlpha(alpha, problem.getLowerBounds(), problem.getUpperBounds());
        Mutation mutation = new NormalDistributionMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, sigma);

        AbstractMOOPAlgorithm nsga3 = new NSGA3(population, problem, crossover, mutation, maxGen, allowRepetition, numberOfDivisions);
        nsga3.run();
//        MOOPUtils.printSolutions(nsga3);
        for (List<Solution> l : nsga3.paretoFronts()){
            for(Solution s : l){
                System.out.println(Arrays.toString(s.getObjectives()));
            }
        }
    }
}
