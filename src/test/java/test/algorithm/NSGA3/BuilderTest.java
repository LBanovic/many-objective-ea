package test.algorithm.NSGA3;

import hr.fer.zemris.zavrsni.algorithms.AlgorithmBuilder;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Builder;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.PolynomialMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.lang.reflect.InvocationTargetException;

public class BuilderTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        MOOPProblem problem = (MOOPProblem) Class.forName("hr.fer.zemris.zavrsni.evaluator.examples.DTLZ1").getDeclaredConstructor(new Class[]{Integer.class}).newInstance(3);
        AlgorithmBuilder<Solution, NSGA3> builder = new NSGA3Builder(problem, 12, false);
        builder.setCrossover(new SBXCrossover<>(new RegularSolutionFactory(), 20, problem.getLowerBounds(),
                problem.getUpperBounds())).setMutation(new PolynomialMutation(problem.getLowerBounds(), problem.getUpperBounds(),
                1. / problem.getNumberOfVariables(), 20));
        NSGA3 nsga3 = builder.getAlgorithm();
        nsga3.run();
        OutputUtils.printSolutions(nsga3);
    }
}
