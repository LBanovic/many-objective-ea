package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.*;

public final class MOOPUtils {

    private static Random rand = new Random();

    //So it cannot be instantiated.
    private MOOPUtils() {
    }

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
     * Performs non dominated sorting on the given population and stores the resulting Pareto fronts
     * in the provided List.
     *
     * @param population
     * @param fronts
     */
    @SuppressWarnings("unchecked")
    public static <T extends Solution> void nonDominatedSorting(List<T> population,
                                                                List<List<T>> fronts) {
        double[] dominatedBy = new double[population.size()];
        List<Integer>[] dominatesOver = (List<Integer>[]) new LinkedList[population.size()];
        fronts.clear();

        List<List<Integer>> indexFronts = new LinkedList<>();
        List<Integer> firstFront = new LinkedList<>();
        indexFronts.add(firstFront);

        for (int i = 0; i < population.size(); i++) {
            dominatedBy[i] = 0;
            dominatesOver[i] = new LinkedList<>();
            for (int j = 0; j < population.size(); j++) {
                if (i != j) {
                    if (dominates(population.get(i), population.get(j))) {
                        dominatesOver[i].add(j);
                    } else if (dominates(population.get(j), population.get(i))) {
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
            List<T> solutionFront = new ArrayList<>(front.size());
            for (int i : front) {
                solutionFront.add(population.get(i));
            }
            fronts.add(solutionFront);
        }
    }

    public static <T extends Solution> boolean dominates(T s1, T s2) {
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

    public static <T extends Solution> void printSolutions(AbstractMOOPAlgorithm<T> a) {
        for (T s : a.getNondominatedSolutions()) {
            System.out.println(Arrays.toString(s.getObjectives()));
        }
    }

    public static <T extends Solution> void printParameters(AbstractMOOPAlgorithm<T> a) {
        for (Solution s : a.getNondominatedSolutions()) {
            System.out.println(s);
        }
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
                if(!childPopulation.contains(child)) childPopulation.add(child);
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
