package test.problemsuite.DTLZ2;

import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.evaluator.examples.DTLZ2;
import hr.fer.zemris.zavrsni.solution.Solution;

public class DTLZ2Test {
    public static void main(String[] args) {
        MOOPProblem dtlz2 = new DTLZ2(4);
        Solution sol = new Solution(new double[]{0.5, 0.4, 0.3, 0.2, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                                    dtlz2.getNumberOfObjectives());
        dtlz2.evaluateObjectives(sol);
        double sum = 0;
        for (int i = 0; i < sol.getObjectives().length; i++){
            sum += sol.getObjectives()[i] * sol.getObjectives()[i];
        }
        System.out.println(sum);
    }
}
