package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Arrays;

public class OutputUtils {
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
}
