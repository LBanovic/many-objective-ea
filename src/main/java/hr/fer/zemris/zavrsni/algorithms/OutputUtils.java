package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.solution.Solution;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class OutputUtils {
    public static <T extends Solution> void printSolutions(AbstractMOOPAlgorithm<T> a) {
        printSolutions(a, System.out);
    }

    public static <T extends Solution> void printSolutions(AbstractMOOPAlgorithm<T> a, PrintStream ps) {
        for (T s : a.getNondominatedSolutions()) {
            ps.print("[");
            for(int i = 0; i < s.getObjectives().length; i++){
                ps.print(s.getObjectives()[i]);
                if(i != s.getObjectives().length - 1) ps.print(", ");
            }
            ps.print("]");
            ps.println();
        }
    }

    public static <T extends Solution> void printParameters(AbstractMOOPAlgorithm<T> a, PrintStream pw) {
        for (Solution s : a.getNondominatedSolutions()) {
            pw.println(s);
        }
    }

    public static <T extends Solution> void printParameters(AbstractMOOPAlgorithm<T> a) {
        printParameters(a, System.out);
    }

    public static <T extends Number> void printNumberList(PrintStream ps, List<List<T>> ref){
        for(List<T> l: ref){
            for(T d: l) ps.print(d +" ");
            ps.println();
        }
    }
}
