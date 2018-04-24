package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

public final class MOOPUtils {

    private static Random rand = new Random();

    //So it cannot be instantiated.
    private MOOPUtils() { }

    /**
     * Evaluates all objectives for every unit in the population.
     *
     * @param solutions the population to evaluate
     * @param problem   the MOOPProblem used to evaluate
     */
    public static void evaluatePopulation(Solution[] solutions, MOOPProblem problem) {
        for (Solution solution : solutions) {
            problem.evaluateObjectives(solution);
        }
    }


    /**
     * Performs non dominated sorting on the given population and stores the resulting Pareto fronts
     * in the provided List.
     *
     * @param population
     * @param fronts
     */
    public static void nonDominatedSorting(Solution[] population, List<List<Solution>> fronts) {
        double[]        dominatedBy   = new double[population.length];
        List<Integer>[] dominatesOver = (List<Integer>[]) new LinkedList[population.length];
        fronts.clear();

        List<List<Integer>> indexFronts = new LinkedList<>();
        List<Integer>       firstFront  = new LinkedList<>();
        indexFronts.add(firstFront);

        for (int i = 0; i < population.length; i++) {
            dominatedBy[i] = 0;
            dominatesOver[i] = new LinkedList<>();
            for (int j = 0; j < population.length; j++) {
                if (i != j) {
                    if (dominates(population[i], population[j])) {
                        dominatesOver[i].add(j);
                    } else if (dominates(population[j], population[i])) {
                        dominatedBy[i]++;
                    }
                }
            }
            if (dominatedBy[i] == 0) {
                firstFront.add(i);
            }
        }

        int frontCounter = 0;
        while (true) {
            List<Integer> nextFront = new LinkedList<>();
            for (int index : indexFronts.get(frontCounter)) {
                for (int j : dominatesOver[index]) {
                    dominatedBy[j]--;
                    if (dominatedBy[j] == 0) {
                        nextFront.add(j);
                    }
                }
            }
            if (nextFront.size() == 0) {
                break;
            }
            frontCounter++;
            indexFronts.add(nextFront);
        }

        for (List<Integer> front : indexFronts) {
            List<Solution> solutionFront = new ArrayList<>(front.size());
            for (int i : front) {
                solutionFront.add(population[i]);
            }
            fronts.add(solutionFront);
        }
    }

    public static boolean dominates(Solution s1, Solution s2) {
        double[] obj1 = s1.getObjectives();
        double[] obj2 = s2.getObjectives();

        for (int i = 0; i < obj1.length; i++) {
            if (obj1[i] < obj2[i]) {
                return false;
            }
        }

        for (int i = 0; i < obj1.length; i++) {
            if (obj1[i] > obj2[i]) {
                return true;
            }
        }

        return false;
    }

    /**
     * Compares the given number with the bounds and returns the according number.
     *
     * @param toConstraint number to compare; returned if within bounds
     * @param lowerBound   lower bound; returned if the compared number is smaller than it
     * @param upperBound   upper bound; returned if the compared number is greater than it
     * @return
     */
    public static double constrainWithinInterval(double toConstraint, double lowerBound, double upperBound) {
        if (toConstraint < lowerBound) {
            return lowerBound;
        }
        if (toConstraint > upperBound) {
            return upperBound;
        }
        return toConstraint;
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
    public static Solution[] generateRandomPopulation(
        int populationSize, double[] lowerBounds, double[] upperBounds,
        int numberOfVariables, int numberOfObjectives
    ) {
        Solution[] population = new Solution[populationSize];
        for (int i = 0; i < populationSize; i++) {
            double[] varArray = new double[numberOfVariables];
            for (int j = 0; j < varArray.length; j++) {
                varArray[j] = rand.nextDouble() * (upperBounds[j] - lowerBounds[j]) + lowerBounds[j];
            }
            population[i] = new Solution(varArray, numberOfObjectives);
        }
        return population;
    }

    public static Solution[] generateRandomPopulation(int populationSize, MOOPProblem problem){
        return generateRandomPopulation(populationSize, problem.getLowerBounds(), problem.getUpperBounds(),
                problem.getNumberOfVariables(), problem.getNumberOfObjectives());
    }

    public static void printSolutions(AbstractMOOPAlgorithm a){
        for (Solution s : a.getNondominatedSolutions()){
            System.out.println(Arrays.toString(s.getObjectives()));
        }
    }

    public static void printParameters(AbstractMOOPAlgorithm a){
        for(Solution s: a.getNondominatedSolutions()){
            System.out.println(s);
        }
    }

    public static Solution[] createNewPopulation(Solution[] population, Selection selection, Crossover crossover,
                                                    Mutation mutation, boolean allowRepetition){
        Solution[] childPopulation = new Solution[population.length];
        for (int i = 0; i < childPopulation.length; i++) {
            Solution p1 = selection.select(population);
            Solution p2;
            do {
                p2 = selection.select(population);
            } while (p2 == p1 && !allowRepetition);
            List<Solution> children = crossover.cross(p1, p2);
            for(Solution sol : children){
                mutation.mutate(sol);
            }
            for(int j = i; i - j < children.size() && i < childPopulation.length; i++){
                childPopulation[i] = children.get(i - j);
            }
            i -= 1;
        }
        return childPopulation;
    }

    public static Solution[] mergePopulations(Solution[] population, Solution[] childPopulation){
        Solution[] combined = new Solution[population.length + childPopulation.length];
        for (int i = 0; i < combined.length; i++) {
            if (i < population.length) combined[i] = population[i];
            else combined[i] = childPopulation[i - population.length];
        }
        return combined;
    }
}
