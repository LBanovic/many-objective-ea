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
                     int closestVectors, int parameterH, Mutation mutation, Crossover<Solution> crossover,
                     int maxGen, double penalty) {
        super(population, problem, closestVectors, parameterH, mutation, crossover, maxGen);
        this.penalty = penalty;

    }

    @Override
    protected double scalarizationFunction(Solution s, double[] weights, double[] ideal) {
        RealVector z = new ArrayRealVector(ideal);
        RealVector f = new ArrayRealVector(s.getObjectives());
        RealVector lambda = new ArrayRealVector(weights);

        double d1 = z.subtract(f).dotProduct(lambda) / lambda.getNorm(),
                d2 = f.subtract(z.subtract(lambda.mapMultiplyToSelf(d1))).getNorm();
        return d1 + penalty * d2;
    }
}
