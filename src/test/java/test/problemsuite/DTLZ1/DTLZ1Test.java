package test.problemsuite.DTLZ1;

import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Arrays;

public class DTLZ1Test {
    public static void main(String[] args) {
        MOOPProblem dtlz1 = new DTLZ1(10);
        Solution    sol   = new Solution(new double[]{0.3, 0.8, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.5, 0.5, 0.5, 0.5, 0.5}, dtlz1.getNumberOfObjectives());
        dtlz1.evaluateObjectives(sol);
        System.out.println(Arrays.toString(sol.getObjectives()));
        double sum = 0;
        for (int i = 0; i < sol.getObjectives().length; i++){
            sum += sol.getObjectives()[i];
        }
        System.out.println(sum);
    }
}
