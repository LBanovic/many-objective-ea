package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulationUtils {
    private static Random rand = new Random();

    /**
     * Evaluates all objectives for every unit in the population.
     *
     * @param solutions the population to evaluate
     * @param problem   the MOOPProblem used to evaluate
     */
    public static <T extends Solution> void evaluatePopulation(List<T> solutions, MOOPProblem problem) {
        for (Solution solution : solutions) {
            problem.evaluateObjectives(solution);
        }
    }

    /**
     * Generates a random population with the given size. Every unit shall conform to the given bounds and their length
     * will be equal to the number of variables.
     *
     * @param populationSize
     * @param lowerBounds
     * @param upperBounds
     * @param numberOfVariables
     * @param numberOfObjectives
     * @return
     */
    public static <T extends Solution> List<T> generateRandomPopulation(
            int populationSize, double[] lowerBounds, double[] upperBounds,
            int numberOfVariables, int numberOfObjectives, SolutionFactory<T> factory
    ) {
        List<T> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            double[] varArray = new double[numberOfVariables];
            for (int j = 0; j < varArray.length; j++) {
                varArray[j] = rand.nextDouble() * (upperBounds[j] - lowerBounds[j]) + lowerBounds[j];
            }
            population.add(factory.create(varArray, numberOfObjectives));
        }
        return population;
    }

    public static <T extends Solution> List<T> generateRandomPopulation(int populationSize, MOOPProblem problem,
                                                                        SolutionFactory<T> factory) {
        return generateRandomPopulation(populationSize, problem.getLowerBounds(), problem.getUpperBounds(),
                problem.getNumberOfVariables(), problem.getNumberOfObjectives(), factory);
    }

    public static <T extends Solution> List<T> createNewPopulation(List<T> population, Selection<T> selection, Crossover<T> crossover,
                                                                   Mutation mutation, boolean allowRepetition) {
        List<T> childPopulation = new ArrayList<>(population.size());
        while (childPopulation.size() < population.size()) {
            T p1 = selection.select(population);
            T p2;
            do {
                p2 = selection.select(population);
            } while (p2 == p1 && !allowRepetition);
            List<T> children = crossover.cross(p1, p2);
            for (Solution sol : children) {
                mutation.mutate(sol);
            }
            for (T child : children) {
                if(!childPopulation.contains(child))
                    childPopulation.add(child);
            }
        }
        while (childPopulation.size() > population.size()) {
            childPopulation.remove(childPopulation.size() - 1);
        }
        return childPopulation;
    }

    public static <T extends Solution> List<T> mergePopulations(List<T> population, List<T> childPopulation) {
        List<T> combined = new ArrayList<>(population);
        combined.addAll(childPopulation);
        return combined;
    }
}
