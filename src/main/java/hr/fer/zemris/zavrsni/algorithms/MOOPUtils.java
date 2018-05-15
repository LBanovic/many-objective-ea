package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.experiments.Experiment;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class MOOPUtils {

    public static final double EPSILON = 1e-9;

    //So it cannot be instantiated.
    private MOOPUtils() {
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


    public static double calculateDistance(Solution s, Solution t) {
        double sum = 0;
        double[] obj1 = s.getObjectives();
        double[] obj2 = t.getObjectives();
        for (int i = 0; i < obj1.length; i++) {
            double dif = obj1[i] - obj2[i];
            sum += dif * dif;
        }
        return Math.sqrt(sum);
    }

    private static int factorial(int n) {
        int multiply = 1;
        for (int i = 1; i <= n; i++) {
            multiply *= i;
        }
        return multiply;
    }

    public static int binomialCoefficient(int n, int k) {
        if (n < k) throw new IllegalArgumentException("n must be greater than k");
        if (k > n / 2) k = n - k;
        int mul = 1;
        for (int i = n - k + 1; i <= n; i++) {
            mul *= i;
        }
        return mul / factorial(k);
    }

    public static MOOPProblem getExample(String exampleName, int exampleSize) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (MOOPProblem) Class.forName("hr.fer.zemris.zavrsni.evaluator.examples." + exampleName).
                getDeclaredConstructor(Integer.class).newInstance(exampleSize);
    }
}
