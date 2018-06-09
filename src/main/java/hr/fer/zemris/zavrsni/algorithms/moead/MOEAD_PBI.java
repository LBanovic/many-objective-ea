package hr.fer.zemris.zavrsni.algorithms.moead;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

public class MOEAD_PBI extends AbstractMOEAD {

    private double penalty;

    public MOEAD_PBI(List<Solution> population, MOOPProblem problem,
                     int closestVectors, List<Integer> parameterH, Mutation mutation, Crossover<Solution> crossover,
                     int maxGen, double penalty) {
        super(population, problem, closestVectors, parameterH, mutation, crossover, maxGen);
        this.penalty = penalty;

    }

    @Override
    protected double scalarizationFunction(Solution s, double[] weights, double[] ideal) {
        double[] sol = s.getObjectives();

        double d1, d2, nl;
        d1 = d2 = nl = 0.0;
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            d1 += (sol[i] - ideal[i]) * weights[i];
            nl += Math.pow(weights[i], 2.0);
        }
        nl = Math.sqrt(nl);
        d1 = Math.abs(d1) / nl;

        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            d2 += Math.pow((sol[i] - ideal[i]) - d1 * (weights[i] / nl), 2.0);
        }
        d2 = Math.sqrt(d2);
        return d1 + penalty * d2;
    }
}
