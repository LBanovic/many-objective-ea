package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Arrays;

public class OutputUtils {
    public static <T extends Solution> void printSolutions(AbstractMOOPAlgorithm<T> a) {
        for (T s : a.getNondominatedSolutions()) {
            System.out.print("[");
            for(int i = 0; i < s.getObjectives().length; i++){
                System.out.print(-s.getObjectives()[i]);
                if(i != s.getObjectives().length - 1) System.out.print(", ");
            }
            System.out.print("]");
            System.out.println();
        }
    }

    public static <T extends Solution> void printParameters(AbstractMOOPAlgorithm<T> a) {
        for (Solution s : a.getNondominatedSolutions()) {
            System.out.println(s);
        }
    }
}
